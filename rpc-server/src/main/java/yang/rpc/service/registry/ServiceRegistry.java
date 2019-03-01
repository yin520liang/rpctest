/**
 * 
 */
package yang.rpc.service.registry;

import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import yang.rpc.config.Constant;

/**
 * @Title ServiceRegistry
 * @Description 服务注册
 * @Date 2018年2月11日
 */
public class ServiceRegistry {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ServiceRegistry.class);
	
	private String address; // address of zookeeper service
	
	private CountDownLatch latch = new CountDownLatch(1);
	
	public ServiceRegistry(String address) {
		this.address = address;
	}
	
	public void register(String data) {
		if(data != null) {
			ZooKeeper zk = connectServer();
			if(zk != null) {
				createNode(zk, data);
			}
		}
	}


	private void createNode(ZooKeeper zk, String data) {
		try {
			byte[] bytes = data.getBytes();
			String path = zk.create(Constant.ZK_DATA_PATH, bytes, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
			LOGGER.debug("create zookeeper node ({} -> {})", path, data);
			
		} catch (Exception e) {
			LOGGER.error("", e);
		}
		
	}

	private ZooKeeper connectServer() {
		ZooKeeper zk = null;
		try {
			zk = new ZooKeeper(address, Constant.ZK_SESSION_TIMEOUT, new Watcher() {
				@Override
				public void process(WatchedEvent e) {
					if(e.getState() == Event.KeeperState.SyncConnected) {
						latch.countDown();
					}			
				}			
			});
			latch.await();
		}catch (Exception e) {
			LOGGER.error("", e);
		}
		return zk;
	}

}
