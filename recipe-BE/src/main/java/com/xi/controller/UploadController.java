package com.xi.controller;

import com.xi.common.grace.GraceJSONResult;
import com.xi.common.utils.VideoUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.apache.commons.lang3.StringUtils;
@Api(tags = "UploadController")
@RestController
public class UploadController {
    @Value("${server-resource.uploadFolder}")
    private String FILE_FOLDER;

    @ApiOperation(value = "上传文件（图片、视频）")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "dirType", value = "目标文件夹"),
        @ApiImplicitParam(name = "fileType", value = "文件类型")
    })
    @PostMapping("/upload")
    public GraceJSONResult uploadFace(@RequestParam("dirType") String dirType, @RequestParam("fileType") String fileType,
                             @RequestParam("file") MultipartFile[] files) throws Exception {


        // 文件保存的命名空间
        String fileSpace = FILE_FOLDER;
        // 保存到数据库中的相对路径
        String uploadPathDB = "/" + dirType;
        String coverUrl="";
        FileOutputStream fileOutputStream = null;
        InputStream inputStream = null;
        try {
            if (files != null && files.length > 0) {

                String fileName = files[0].getOriginalFilename();
                System.out.println("fileName:"+fileName);
                if (StringUtils.isNotBlank(fileName)) {
                    // 文件上传的最终保存路径
                    String finalFacePath = fileSpace + uploadPathDB + "/" + fileName;
                    // 设置数据库保存的路径
                    uploadPathDB += ("/" + fileName);

                    File outFile = new File(finalFacePath);
                    if (outFile.getParentFile() != null || !outFile.getParentFile().isDirectory()) {
                        // 创建父文件夹
                        outFile.getParentFile().mkdirs();
                    }

                    fileOutputStream = new FileOutputStream(outFile);
                    inputStream = files[0].getInputStream();
                    IOUtils.copy(inputStream, fileOutputStream); //inputStream复制到fileOutputStream并写入磁盘
                    if(fileType.equals("video")){
                        String uuId = UUID.randomUUID().toString().replaceAll("-", "");
                        coverUrl = "/" +  dirType + "/"+ uuId +".jpg";

                        String[] params = { dirType, uuId};
                        String fileUrl = StringUtils.join( params, File.separator);

                        System.out.println("start to get Cover img!!coverUrl:"+coverUrl);
                        String s = VideoUtils.fetchFrame(FILE_FOLDER + uploadPathDB);
                        VideoUtils.base64ToMultipart(s).transferTo(new File(FILE_FOLDER +  fileUrl + ".jpg"));
                    }else{
                        coverUrl = uploadPathDB;
                    }
                }

            } else {
                return GraceJSONResult.errorMsg("上传出错...");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return GraceJSONResult.errorMsg("上传出错...");
        } finally {
            if (fileOutputStream != null) {
                fileOutputStream.flush();
                fileOutputStream.close();
            }
        }

        Map<String, String> uploadInfo = new HashMap<>();
        uploadInfo.put("fileType",fileType);
        uploadInfo.put("fileUrl", uploadPathDB);
        uploadInfo.put("coverUrl", coverUrl);
        return GraceJSONResult.ok(uploadInfo);
    }

}
