新版的数据库连接池工具,基于Druid连接池,实现简单增删改查以及简单事务的管理
使用过程中注意在所有数据库操作执行完毕以后注意连接池的释放
在Oracle的连接中,有三种连接方式
1.SID:支持
2.serviceName:支持
3.TNSName:支持
配置文件中指定url,包括ip, port ,以及database

