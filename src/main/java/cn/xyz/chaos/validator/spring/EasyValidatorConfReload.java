package cn.xyz.chaos.validator.spring;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executors;

import name.pachler.nio.file.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;

import cn.xyz.chaos.validator.config.XmlValidatorResolver;

/**
 * 自动重读配置：IntelliJ下修改配置后需要按Ctrl+F9（Make Project）
 * jdk1.7以后可以通过nio2的WatchService实现，这里使用jpatchwatch实现
 * <bean id="validatorConfReload" class="cn.xyz.chaos.validator.spring.EasyValidatorConfReload" />
 */
@Profile({ "development", "develop", "dev" })
public class EasyValidatorConfReload {
	private final Logger	logger	= LoggerFactory.getLogger(getClass());

	public EasyValidatorConfReload() {
		Executors.newSingleThreadExecutor().submit(new ConfReload());
	}

	class ConfReload implements Runnable {
		@Override
		public void run() {
			String cfgXmlDirectory = new File(XmlValidatorResolver.class.getClassLoader()
					.getResource(XmlValidatorResolver.VALIDATOR_FILE).getPath()).getParent();
			logger.info("配置变更检查：", cfgXmlDirectory);
			WatchService watchService = FileSystems.getDefault().newWatchService();
			Path path = Paths.get(cfgXmlDirectory);
			WatchKey key = null;
			try {
				key = path.register(watchService, StandardWatchEventKind.ENTRY_CREATE,
						StandardWatchEventKind.ENTRY_DELETE, StandardWatchEventKind.ENTRY_MODIFY, StandardWatchEventKind.OVERFLOW);
			} catch (UnsupportedOperationException uox) {
				logger.error("filehing not supported!", uox);
				// handle this error here
			} catch (IOException iox) {
				logger.error("I/Ors", iox);
				// handle this error here
			}

			for (;;) {
				// take() will block until a file has been created/deleted
				WatchKey signalledKey;
				try {
					signalledKey = watchService.take();
				} catch (InterruptedException ix) {
					// we'll ignore being interrupted
					continue;
				} catch (ClosedWatchServiceException cwse) {
					// other thread closed watch service
					logger.info("watchice closed, terminating.");
					break;
				}
				// get list of events from key
				List<WatchEvent<?>> list = signalledKey.pollEvents();
				// VERY IMPORTANT! call reset() AFTER pollEvents() to allow the
				// key to be reported again by the watch service
				signalledKey.reset();
				// we'll simply print what has happened; real applications
				// will do something more sensible here
				for (WatchEvent e : list) {
					String message = "";
					if (e.kind() == StandardWatchEventKind.ENTRY_CREATE) {
						Path context = (Path) e.context();
						message = context.toString() + " created";
					} else if (e.kind() == StandardWatchEventKind.ENTRY_DELETE) {
						Path context = (Path) e.context();
						message = context.toString() + " deleted";
					} else if (e.kind() == StandardWatchEventKind.ENTRY_MODIFY) {
						Path context = (Path) e.context();
						message = context.toString() + " modified";
					} else if (e.kind() == StandardWatchEventKind.OVERFLOW) {
						message = "OVERFLOW: more changes happened than we could retreive";
					}
					logger.info(message);
				}
				EasyValidator.getUtilities().getContext().initializeContext();
			}
		}
	}

}