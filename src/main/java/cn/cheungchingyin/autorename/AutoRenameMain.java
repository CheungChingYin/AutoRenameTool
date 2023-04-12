package cn.cheungchingyin.autorename;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author 张正贤
 * @Date 2023/4/12 8:08
 * @Version 1.0
 */
@Slf4j
public class AutoRenameMain {

    public static void main(String[] args) {
        InputStream propertiesInputStream;
        // 获得配置信息，没有则默认获得项目的配置
        Properties properties = new Properties();
        try {
            // 获取所在jar包同一级文件夹内的application.properties
            File config = new File("application.properties");
            // 存在则使用自定义配置文件
            if (config.exists()) {
                log.info("------使用自定义的application.properties,路径为{}------", config.getAbsolutePath());
                propertiesInputStream = new BufferedInputStream(Files.newInputStream(config.toPath()));
            } else {
                propertiesInputStream = ClassLoader.getSystemClassLoader().getResourceAsStream("application.properties");
                log.info("------使用系统默认的的application.properties,路径为{}------", config.getAbsolutePath());
            }
            // 读取配置文件
            properties.load(new BufferedReader(new InputStreamReader(propertiesInputStream, StandardCharsets.UTF_8)));
        } catch (Exception e) {
            log.error(e.getMessage());
            log.error("读取配置文件失败");
            return;
        }
        // 源地址
        String srcDirPath = properties.getProperty("srcDirPath");
        // 输出文件的目的地址
        String destDirPath = properties.getProperty("destDirPath");
        // 检查配置是否为空
        if (StrUtil.isBlank(srcDirPath) || StrUtil.isBlank(destDirPath)) {
            log.error("配置文件的源目录地址和目的目录地址不能为空");
            return;
        }
        // 去除路径最后的斜杠，避免由于配置问题加多一层文件夹
        if (srcDirPath.endsWith("/") || srcDirPath.endsWith("\\")) {
            srcDirPath = srcDirPath.substring(0, srcDirPath.length() - 1);
        }
        if (destDirPath.endsWith("/") || destDirPath.endsWith("\\")) {
            destDirPath = destDirPath.substring(0, srcDirPath.length() - 1);
        }
        log.info("------即将处理{}下的所有文件------", srcDirPath);
        log.info("------正在统计文件个数------", srcDirPath);
        // 获得文件夹下所有文件
        List<File> fileList = FileUtil.loopFiles(srcDirPath);
        log.info("------需要处理的文件个数为 {} 个------", fileList.size());
        log.info("------即将开始处理，按Y进行确认------", fileList.size());
        Scanner scanner = new Scanner(System.in);
        if (!scanner.next().equalsIgnoreCase("y")) {
            log.info("------即将取消------", fileList.size());
            return;
        }
        log.info("------正在处理，请勿关闭软件和电源------");
        String finalDistDirPath = destDirPath;
        // 遍历文件
        fileList.forEach(file -> {
            Path filePath = file.toPath();
            // 文件创建时间
            Date createDate = new Date();
            try {
                // 获得文件配置信息
                BasicFileAttributes fileAttributes = Files.readAttributes(filePath, BasicFileAttributes.class);
                // 文件创建时间
                FileTime fileTime = fileAttributes.creationTime();
                createDate.setTime(fileTime.toMillis());
            } catch (IOException e) {
                log.error("------读取文件属性信息失败，文件地址：{}------", file.getAbsolutePath());
                throw new RuntimeException(e);
            }
            // 根据输出地址/yyyy/yyyy-MM-dd的格式进行归档
            File destFile = new File(finalDistDirPath+ "\\" + DateUtil.format(createDate, "yyyy") + "\\" + DateUtil.format(createDate, "yyyy-MM-dd") + "\\" + file.getName());
            log.info(destFile.getAbsolutePath());
            // 输出到目的地址
            FileUtil.copy(file, destFile, true);
        });
        log.info("------文件整理完成------");
    }
}
