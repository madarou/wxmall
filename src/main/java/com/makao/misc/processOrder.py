#!/usr/bin/env python
# encoding: utf-8
'''
demo -- shortdesc
'''
#from urllib2 import Request, urlopen, URLError 
import sys
import os
import urllib 
import urllib2
import json
import constant
import MySQLdb
from log import logger
import time

def sleeptime(hour,min,sec):
    return hour*3600 + min*60 + sec;

def main(argv=None):
#    process(1,1)
    second = sleeptime(0,1,0);
    while 1==1:
        (msg,orders)=approachOrders("1",'fdsfdsfds')
        print msg
        print orders
        logger.info(msg)
        logger.info(orders)
        if msg !=200:
            #vendor登录查找失败，发送通知
            pass
        time.sleep(second);
        
def approachOrders(vendorid,token):
    '''从数据库中找出需要将状态从排队中改为待处理的订单，将其状态设为待处理
        当配送时间起点-准备时间<=当前时间时的订单满足条件'''
    url = constant.DOMAIN+'/orderOn/vapproach/'+vendorid
    user_agent = 'Mozilla/4.0 (compatible; MSIE 5.5; Windows NT)' 
    headers = { 'User-Agent' : user_agent, 'token':token } 
    req = urllib2.Request(url, None, headers) 
    try: 
        response = urllib2.urlopen(req) 
    except urllib2.URLError, e: 
        if hasattr(e, 'reason'): 
            print 'We failed to reach a server.' 
            print 'Reason: ', e.reason 
            logger.info("We failed to reach a server. Reason: "+e.reason)
            #发送通知
        elif hasattr(e, 'code'): 
            print 'The server couldnt fulfill the request.' 
            print 'Error code: ', e.code 
            logger.info("The server couldnt fulfill the request. Error code: "+e.code)
            #发送通知
    else: 
        # everything is fine
        result = json.loads(response.read())
        return (result['msg'],result['orders'])
    
def getOrders():
    '''从数据库中找出需要将状态从排队中改为待处理的订单的cityid,areaid和orderid，
    当配送时间起点-准备时间<=当前时间时的订单满足条件，被加入返回列表中'''
    
    
def process(vendorid,orderid):
    '''将订单号从排队中改为待处理'''
    url = constant.DOMAIN+'/orderOn/vprocess/'+vendorid+'?orderid='+orderid
    user_agent = 'Mozilla/4.0 (compatible; MSIE 5.5; Windows NT)' 
    headers = { 'User-Agent' : user_agent } 
#    values = {'name' : 'Michael Foord', 
#              'location' : 'pythontab', 
#              'language' : 'Python' } 
#    data = urllib.urlencode(values) 
    req = urllib2.Request(url, None, headers)  
    try: 
        response = urllib2.urlopen(req) 
    except urllib2.URLError, e: 
        if hasattr(e, 'reason'): 
            print 'We failed to reach a server.' 
            print 'Reason: ', e.reason 
        elif hasattr(e, 'code'): 
            print 'The server couldnt fulfill the request.' 
            print 'Error code: ', e.code 
    else: 
        # everything is fine
        result = json.loads(response.read())
        print result['msg']
        return result['msg']

if __name__ == "__main__":
    logger.info("-----------begin approach orders-----------")
    main()
    logger.info("-----------finish approach orders-----------")