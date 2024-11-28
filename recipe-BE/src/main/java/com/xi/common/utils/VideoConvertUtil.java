//package com.xi.common.utils;
//
//import lombok.extern.slf4j.Slf4j;
//import org.bytedeco.javacpp.avcodec;
//import org.bytedeco.javacpp.avutil;
//import org.bytedeco.javacv.*;
//
///**
// * @ClassName :VideoConvertUtil
// * @Description :
// */
//@Slf4j
//public class VideoConvertUtil {
//    public static void convert(String inputFile, String outputFile) throws Exception {
//        System.out.println("转码开始");
//        FFmpegFrameGrabber grabber = FFmpegFrameGrabber.createDefault(inputFile);
//        Frame captured_frame;
//        FFmpegFrameRecorder recorder = null;
//
//        try {
//            grabber.start();
//            int videoCodec = grabber.getVideoCodec();
//            log.info("videoCodec:"+videoCodec);
//            /**
//             * ImageWidth：视频宽
//             * ImageHeight：视频高
//             * audioChannels:设置重新编码的音频流中使用的声道数（1 =单声道，2 = 双声道（立体声））。如果未设置任何声道值，则编码器将选择默认值 0。
//             */
//            recorder = new FFmpegFrameRecorder(outputFile, grabber.getImageWidth(), grabber.getImageHeight(), grabber.getAudioChannels());
////            recorder.setImageHeight(640);
////            recorder.setImageWidth(480);
//            recorder.setVideoCodec(avcodec.AV_CODEC_ID_MPEG4);
//            recorder.setFormat("mp4");
//            // 视频帧率(保证视频质量的情况下最低25，低于25会出现闪屏)
//            recorder.setFrameRate(grabber.getFrameRate());
//            recorder.setSampleRate(grabber.getSampleRate());
//            //视频编码属性配置 H.264 H.265 MPEG
//            recorder.setVideoCodec(avcodec.AV_CODEC_ID_H264);
//            //recorder.setVideoCodecName("libx264");
//            //设置视频比特率,单位:b
//            recorder.setVideoBitrate(grabber.getVideoBitrate());
//            recorder.setAspectRatio(grabber.getAspectRatio());
//            // yuv420p,像素
//            recorder.setPixelFormat(avutil.AV_PIX_FMT_YUV420P);
//            //设置视频质量
////            recorder.setVideoQuality(avutil.FF_LAMBDA_MAX);
//            recorder.setVideoQuality(avutil.FF_LAMBDA_SHIFT);
//            // 设置音频通用编码格式
//            recorder.setAudioCodec(avcodec.AV_CODEC_ID_AAC);
//            //设置音频比特率,单位:b (比特率越高，清晰度/音质越好，当然文件也就越大 128000 = 182kb)
//            recorder.setAudioBitrate(grabber.getAudioBitrate());
//            recorder.setAudioOptions(grabber.getAudioOptions());
//
//            recorder.start();
//
//            while (true) {
//                captured_frame = grabber.grabFrame();
//                log.info("转码循环---"+System.currentTimeMillis());
//                if (captured_frame == null) {
//                    log.info("转码完成");
//                    break;
//                }
//                recorder.record(captured_frame);
//            }
//
//        } catch (FrameRecorder.Exception e) {
//            e.printStackTrace();
//        } finally {
//            if (recorder != null) {
//                try {
//                    recorder.close();
//                } catch (Exception e) {
//                    log.info("recorder.close异常" + e);
//                }
//            }
//
//            try {
//                grabber.close();
//            } catch (FrameGrabber.Exception e) {
//                log.info("frameGrabber.close异常" + e);
//            }
//        }
//    }
//
//    public static void main(String[] args) {
//        long startTime = System.currentTimeMillis();
////        String url = "/Users/Downloads/视频/视频 (1)/最新-合片剪辑的3分17.mp4";
////        String url = "/Users/Downloads/视频/健康资讯视频.mp4";
//        String url = "C:\\Users\\86138\\Videos\\油焖基围虾 .mp4";
//        String videoSavePath = "C:\\Users\\86138\\Videos\\afterCovert .mp4";
//        try {
//            VideoConvertUtil.convert(url, videoSavePath);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        long endTime = System.currentTimeMillis();
//        log.info("耗时："+(endTime - startTime));
//    }
//
//}