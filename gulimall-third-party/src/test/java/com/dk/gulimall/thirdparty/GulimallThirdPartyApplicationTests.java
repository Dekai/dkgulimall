package com.dk.gulimall.thirdparty;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.PutObjectResult;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

@SpringBootTest
class GulimallThirdPartyApplicationTests {


	@Autowired
	private OSSClient ossClient;

	@Test
	void fileUpload()  {
		InputStream inputStream = null;
		try {
			inputStream = new FileInputStream("/Users/dekaizhang/Downloads/WechatIMG103.jpeg");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		PutObjectResult yanmall = ossClient.putObject("yanmall", "test123.jpg", inputStream);

		ossClient.shutdown();

		System.out.println("Uploaded...." + yanmall.getETag());
	}

//	@Test
	void contextLoads() {
	}

}
