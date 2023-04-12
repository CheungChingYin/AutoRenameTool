# 1.简介
本工具用于自动整理指定文件夹下的文件，根据文件的创建时间，以 `yyyy/yyyy-MM-dd` 的格式进行自动归类。

### 1.1 环境要求
- JDK >= 1.8

# 2.如何使用

### 2.1 下载压缩安装包

通过 [Release](https://github.com/CheungChingYin/AutoRenameTool/releases) 下载对应的安装包

### 2.2 解压压缩包

压缩包内容如下


### 2.3 配置`application.properties`文件

在JAR包的同一级目录下，创建`application.properties`（压缩包已经自带，无需再手动创建）

![image](https://user-images.githubusercontent.com/20316736/231350547-7ff1979d-9ff4-4606-9ea4-799fe2754695.png)

打开`application.properties` 进行配置

![image](https://user-images.githubusercontent.com/20316736/231351092-1f2da435-3866-4908-af27-b203d758c2f6.png)

`srcDirPath` 是指源文件夹（需要整理的文件夹）

`destDirPath` 目的文件夹（需要输出到哪个文件夹）

需要注意的是，路径分隔符使用 `\` 和 `/` 皆可，但是使用 `\` 的时候必须要注意进行转义为 `\\`。

# 3.二次开发

此项目欢迎各位进行修正bug和二次开发，二次开发请fork本项目，在`dev`分支基础上上新建新分支进行二次开发，开发后可以发起push Request项目的`dev`分支，确认无误后会将代码进行合并。

[项目地址](https://github.com/CheungChingYin/AutoRenameTool)
