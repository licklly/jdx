# JDX

## 📢 特别声明:

1. 本仓库涉及的任何解锁和解密分析脚本或代码，仅用于测试和学习研究，禁止用于商业用途，不能保证其合法性，准确性，完整性和有效性，请根据情况自行判断.
2. 请勿将本项目的任何内容用于商业或非法目的，否则后果自负.
3. 如果任何单位或个人认为该项目的脚本可能涉嫌侵犯其权利，则应及时通知并提供身份证明，所有权证明，我们将在收到认证文件后删除相关代码.
4. 任何以任何方式查看此项目的人或直接或间接使用本仓库项目的任何脚本的使用者都应仔细阅读此声明。rubyangxg 保留随时更改或补充此免责声明的权利。一旦使用并复制了任何本仓库项目的规则，则视为您已接受此免责声明.
5. 您必须在下载后的24小时内从计算机或手机中完全删除以上内容.
6. 您使用或者复制了本仓库且本人制作的任何脚本，则视为已接受此声明，请仔细阅读

## 🐳 安装说明

本项目已打包成`docker`镜像，拉取配置即可使用
> docker安装方法不再赘述

### 1. 拉取并运行docker

```dockerfile
docker run -d \
    -v <config dir>:/jdx/config \
    -p <port>:80 \
    --restart=always \
    --name jdx registry.cn-hangzhou.aliyuncs.com/yiidii-hub/jdx:v0.1.9
```
> 这里命令自行替换卷和端口映射
> 
> 例如：
> ```dockerfile
> docker run -d \
>   -v  /data/jdx/config:/jdx/config \
>   -p 5702:80 \
>   --restart=always \
>   --name jdx registry.cn-hangzhou.aliyuncs.com/yiidii-hub/jdx:v0.1.9
> ```
> 
注意：
 - 记得放行端口

### 2. 访问
这时候访问 `http://ip:port/` 就能访问了

### 3. 后台登录
访问 `http://ip:port/admin`
首次登录用户名：`admin`, 密码：`123465`, **千万记得修改密码！！！！！**

## 📃 使用说明
1. QL配置只能删除和新增，不能编辑操作
2. 所有涉及编辑和删除的操作，左滑即可（就像微信删除最近联系人一样...）

## 📌 一对一推送
**说明：**
> 目前一对一推送是与任务脚本强耦合的，需要在任务的单个cookie运行完之后，所以需要加上一对一通知代码。所以需要维护。

目前仅支持：
- 京东资产变动
- Cookie失效检测


**配置方法:**
1. 先拉库
	```shell
	ql repo https://github.com/wangyiidii/jdx.git "jd_" "" "sendNotify"
	```
	这里只拉取了我改后的`jd_任务`和`sendNotify.js`文件。如果自己拉的其他库里面有`sendNotiy.js`文件，记得自己删除下。
2. 配置QL配置
	在QL后台配置文件下配置：
	```
	# JDX的回调地址，自行替换ip 和 port
	export JDX_URL="http://ip:port/api/third/qlNotify"
	# 当前QL的wxPusherAppToken，可不写，不写的话会以JDX后台配置的wxPusherAppToken生效
	export JDX_WX_PUSHER_APP_TOKEN=""
	```
	
	



## 🥂 更新说明
1. 停止并删除容器
```shell
docker stop jdx && docker rm jdx
```
2. 根据最新版本号跑一个新的容器即可