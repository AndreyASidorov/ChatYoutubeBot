package ru.Sidorov.YoutubeBot;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import ru.Sidorov.YoutubeBot.dto.ResponseByNameChannel;
import ru.Sidorov.YoutubeBot.service.commands.ChangelInfo;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import static ru.Sidorov.YoutubeBot.YoutubeBotApplication.apiKey;


class YoutubeBotApplicationTests {

	@Test
	void testPropertyFile() {
		Properties properties = new Properties();
		try {
			properties.load(new FileInputStream("bot.properties"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Assert.assertTrue(!properties.getProperty("username").isEmpty());
	}

	@Test
	void testFindByChannelName(){
		Properties properties = new Properties();
		try {
			properties.load(new FileInputStream("bot.properties"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		apiKey = properties.getProperty("apiKey");
		ResponseEntity<ResponseByNameChannel> response = ChangelInfo.changelInfoResponseByNameChannel("Шахматы для всех", apiKey);
		Assert.assertTrue(response.getBody().getItems().size() > 0);
	}

}
