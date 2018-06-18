#!/bin/bash
echo "******************************判断是否需要加载json格式化插件jq**********************************"
jqPath="/usr/bin/jq"
echo "jqPath:$jqPath"
if [ -a $jqPath ];then
echo "******************************jq已存在**********************************"
else
wget -O jq https://github.com/stedolan/jq/releases/download/jq-1.5/jq-linux64
chmod +x ./jq
cp jq /usr/bin
rm -f jq
fi
#初始化参数
echo "******************************初始化参数开始**********************************"
#初始化错误码
ERROR_CODE=2

initUrl=$1
echo "$initUrl"
initParamInfo=`curl --connect-timeout 60 $initUrl`
echo "$initParamInfo"

#1.用户编号
USERID=`echo $initParamInfo|jq -r '.data.userId'`
echo "用户编号:$USERID"
if [ -z "$USERID" -o "$USERID" == "null" ];then
exit $ERROR_CODE;
fi

#2.服务编号
SRVID=`echo $initParamInfo|jq -r '.data.srvId'`
echo "服务编号:$SRVID"
if [ -z "$SRVID" -o "$SRVID" == "null" ];then
exit $ERROR_CODE;
fi

#3.版本号
VERSION=`echo $initParamInfo|jq -r '.data.version'`
echo "版本号:$VERSION"
if [ -z "$VERSION" -o "$VERSION" == "null" ];then
exit $ERROR_CODE;
fi

#4.war/jar包生成的相对路径
PROJECT_COMPRESS=`echo $initParamInfo|jq -r '.data.projectCompress'`
echo "相对路径:$PROJECT_COMPRESS"
if [ -z "$PROJECT_COMPRESS" -o "$PROJECT_COMPRESS" == "null" ];then
exit $ERROR_CODE;
fi

#5.共享存储中workspace路径
WORKSPACE_PATH=`echo $initParamInfo|jq -r '.data.workspacePath'`
echo "workspace路径:$WORKSPACE_PATH"
if [ -z "$WORKSPACE_PATH" -o "$WORKSPACE_PATH" == "null" ];then
exit $ERROR_CODE;
fi

#6.镜像url
IMAGE_URL=`echo $initParamInfo|jq -r '.data.imageUrl'`
echo "镜像url:$IMAGE_URL"
if [ -z "$IMAGE_URL" -o "$IMAGE_URL" == "null" ];then
exit $ERROR_CODE;
fi

echo "******************************初始化参数结束**********************************"
if [ -d "${WORKSPACE_PATH}/${PROJECT_COMPRESS}" ];then
cd $WORKSPACE_PATH/$PROJECT_COMPRESS
else
echo "${WORKSPACE_PATH}/${PROJECT_COMPRESS}该路径不存在！"
exit $ERROR_CODE;
fi


echo "******************************创建镜像版本和dockerfile**********************************"
imageInfo=`curl --connect-timeout 60 http://app.api.paas.jsict.local:8082/service/$USERID/$SRVID/createImageVersionAndDockerfile/$VERSION`
echo "返回信息：$imageInfo"
IMAGE_VERSION_ID=`echo $imageInfo|jq -r '.data'`
if [ -z "$IMAGE_VERSION_ID" -o "$IMAGE_VERSION_ID" == "null" ];then
exit $ERROR_CODE;
else
echo "镜像版本编号:$IMAGE_VERSION_ID"
fi

echo "******************************创建服务版本**********************************"
srvVersionInfo=`curl --connect-timeout 60 http://app.api.paas.jsict.local:8082/service/$USERID/$SRVID/createSrvVersion/$VERSION/$IMAGE_VERSION_ID`
echo "返回信息：$srvVersionInfo"
SRV_VERSION_ID=`echo $srvVersionInfo|jq -r '.data'`
if [ -z "$SRV_VERSION_ID" -o "$SRV_VERSION_ID" == "null"  ];then
exit $ERROR_CODE;
else
echo "服务版本编号:$SRV_VERSION_ID"
fi


echo "******************************获取dockerfile**********************************"
DOCKERFILE=`curl --connect-timeout 60 http://image.api.paas.jsict.local:8081/image/$USERID/getVersionDockerfile/$IMAGE_VERSION_ID`
echo "Dockerfile:$DOCKERFILE"
rm -rf Dockerfile
echo -e "${DOCKERFILE//\"/}" > Dockerfile

echo "******************************构建镜像开始**********************************"
docker build -t $IMAGE_URL .
echo "******************************构建镜像结束**********************************"

echo "******************************推入仓库开始**********************************"
docker push $IMAGE_URL
echo "******************************推入仓库结束**********************************"

echo "******************************更新镜像大小**********************************"
imageSize=`docker inspect $IMAGE_URL|jq -r '.[0].Size'`
imageSize=`echo | awk "{printf(\"%.1f\", $imageSize/1000000)}"`
echo "imageSize:$imageSize"

updateImageInfo=`curl --connect-timeout 60 -X PUT  -H "Content-Type: application/json" -d "{\"imageVersionId\":$IMAGE_VERSION_ID,\"imageVersionSize\":$imageSize,\"imageStatus\":1052600}" 'http://image.api.paas.jsict.local:8081/image/version/$USERID/update'`
echo "返回信息：$updateImageInfo"
isSuccess=`echo $srvVersionInfo|jq -r '.success'`
echo "镜像版本编号:$isSuccess"

if [ "$isSuccess" == "1" ];then
docker rmi -f $IMAGE_URL
fi

curl --connect-timeout 60 -X PUT  -H "Content-Type: application/json" -d "{\"srvVersionId\":$SRV_VERSION_ID,\"srvId\":$SRVID}" "http://app.api.paas.jsict.local:8082/service/$USERID/srv/deployment/autoPublish"
