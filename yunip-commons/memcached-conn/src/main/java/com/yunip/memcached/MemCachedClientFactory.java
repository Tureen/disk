package com.yunip.memcached;

import com.meetup.memcached.MemcachedClient;
import com.meetup.memcached.SockIOPool;
import com.yunip.system.ResourcesSetting;

public class MemCachedClientFactory { 
    private static MemcachedClient mcc = new MemcachedClient();  
    static {
        String[] servers =  getServers(ResourcesSetting.getSetting("servers"));
           
        //Integer[] weights = { 3, 2 };   
        SockIOPool pool = SockIOPool.getInstance();  
        pool.setServers( servers );  
        //pool.setWeights( weights ); 
        pool.setNagle( false );  
        pool.setSocketTO( 3000 );  
        pool.setSocketConnectTO( 0 );  
        pool.initialize();  
    }  
    
    public static MemcachedClient getMemcachedClient() {
        return  mcc;
    }  
    
    private static String[] getServers(String servers){
    	if(servers!=null){
    		if(servers.indexOf(";")>=1) {
    			return servers.split(";");
    		}else{
    			String[] serverstemp = {servers};
    			return serverstemp;
    		}
    	}
    	return null;
    }
}
