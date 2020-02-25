package com.nuoquan.scheduler;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.nuoquan.mapper.ArticleMapper;
import com.nuoquan.mapper.ArticleMapperCustom;
import com.nuoquan.service.ArticleService;
import com.nuoquan.service.VoteService;

/**
 * 热度计算定时任务
 * @author jerrio
 */
@Component
@Configuration      // 主要用于标记配置类，兼备 Component 的效果。
@EnableScheduling   // 开启定时任务
@EnableAsync        // 开启多线程
public class MySchedular {
	
	@Autowired
	private ArticleService articleService;
	
	@Autowired
	private VoteService voteService;
	
	@Async
	@Scheduled(cron = "0 */10 * * * ?") // 间隔10分钟
	public void articlePupulorityAutoUpdate() {
		System.err.println("按公式更新文章热度值... " + LocalDateTime.now());
		articleService.upadtePopByFunction();
	}
	
	@Async
	@Scheduled(cron = "0 */8 * * * ?")
	public void autoUpdateVoteStatus(){
		System.err.println("自动判断投票是否过期... " + LocalDateTime.now());
		voteService.autoUpdateVoteStatus();
	}
	
	

//	 Example Code...
//	 @Async
//     @Scheduled(fixedDelay = 1000)  //间隔1秒
//     public void first() throws InterruptedException {
//         System.out.println("第一个定时任务开始 : " + LocalDateTime.now().toLocalTime() + "\r\n线程 : " + Thread.currentThread().getName());
//         System.out.println();
//         Thread.sleep(1000 * 10);
//     }
//
//     @Async
//     @Scheduled(fixedDelay = 2000)
//     public void second() {
//         System.out.println("第二个定时任务开始 : " + LocalDateTime.now().toLocalTime() + "\r\n线程 : " + Thread.currentThread().getName());
//         System.out.println();
//     }

}
