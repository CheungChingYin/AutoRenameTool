package cn.cheungchingyin.autorename;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.Date;
import java.util.List;

/**
 * @Author 张正贤
 * @Date 2023/4/17 16:45
 * @Version 1.0
 */
@Slf4j
public class CopyFileTask implements Runnable{

    private List<File> fileList;
    private String finalDistDirPath;

    public CopyFileTask(List<File> fileList, String finalDistDirPath) {
        this.fileList = fileList;
        this.finalDistDirPath = finalDistDirPath;
    }

    @Override
    public void run() {
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
            File destFile = new File(finalDistDirPath + "\\" + DateUtil.format(createDate, "yyyy") + "\\" + DateUtil.format(createDate, "yyyy-MM-dd") + "\\" + file.getName());
            log.info(destFile.getAbsolutePath());
            // 输出到目的地址
            FileUtil.copy(file, destFile, true);
        });
    }
}
