package org.forwardingproxy.cron;

import java.util.List;

import org.forwardingproxy.dto.AnalysedLog;
import org.forwardingproxy.service.AnalysingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class AnalysingScheduler {

	@Autowired
	private AnalysingService analysingService;

	@Scheduled(fixedRateString = "${anlayse.cron.fixedRate}", initialDelay = 1000)
	public void scheduleFixedRateTaskAsync() {
		List<AnalysedLog> analysedLogs = analysingService.findUnanalyzedLog();
		if (analysedLogs != null) {
			for (AnalysedLog analysedLog : analysedLogs) {
				if (analysedLog != null) {
					analysingService.update(analysedLog);
				}
			}
		}
	}
}
