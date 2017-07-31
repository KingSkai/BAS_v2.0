package cn.itcast.bos.quartz;

import java.util.Date;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import cn.itcast.bos.service.take_delivery.PromotionService;

public class PromotionJob implements Job{

	@Autowired
	private PromotionService promotionService;
	
	@Override
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		//System.out.println("PromotionJob 活动过期程序执行");
		// 每分钟执行一次, 当前时间大于promotion数据表的endData则表示活动已过期,设置status='2'
		promotionService.updateStatus(new Date()); 
	}
	
}
