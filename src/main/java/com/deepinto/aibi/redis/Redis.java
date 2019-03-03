package com.deepinto.aibi.redis;

/**
 * @author Ming
 * @描述 Redis
 * @描述 Redis可能会丢数据, 比如突然断电
 * @安装包 zzlinuxinstallpackage目录下redis-3.0.0.tar.gz
 */
public class Redis {

    /**
     * 一.Redis的安装
     */
    /*
     * @1 Redis 是C语言开发的,安装Redis需要C语言环境,如果没有gcc需要在线安装
     *
     * yum install -y gcc-c++
     *
     * @2 安装步骤,
     *
     * @2.1 redis的源码包上传到linux系统
     *
     * @2.2 解压缩redis
     *
     * @2.3 编译, 进入redis源码目录
     * 执行命令 make
     *
     * @2.4 安装
     *
     * 执行命令 make install PREFIX=/usr/local/redis
     *
     * <PREFIX参数指定redis的安装目录,一般软件安装到/usr目录下>
     * */

    /**
     * @二.redis启动
     */
    /*
     * @1 进入make后的Redis目录进入bin文件夹
     *
     * <示例中的目录为 /usr/local/redis/bin>
     *
     * @2 执行命令 ./redis-server 出现小奶酪图标表示Redis已启动成功
     *
     * @3 结束当前Redis ctrl+c<windows拷贝的快捷键>
     * */


    /**
     * @三.Redis 后端启动
     */
    /*
     * @1 后端启动需要 redis.conf配置文件
     *
     * @2 redis.conf 文件在redis编译目录下<编译目录即是第一步中的解压缩文件目录>
     *
     * @3 执行命令 cp redis.conf /usr/local/redis/bin/
     *
     * @4 进入bin目录, 编辑redis.conf
     *
     * 命令 vim redis.conf
     *
     * @5 将 daemonize no节点更改为 daemonize yes
     *
     * vim下的查找命令:在vim不输入的情况下执行命令 /daemonize
     *
     * @6 后台启动redis ./redis-server redis.conf
     *
     * @7 查看redis是否在后端启动成功
     *
     * 命令 ps aux|grep redis
     *
     * 出现 root  4199  0.6  0.3 137448  7480 ?   Ssl  05:55   0:00 ./redis-server *:6379 表示redis启动成功
     * */

    /**
     * @四.关闭后台的redis
     */
    /*
     * @1 第一种进程关闭 不建议使用kill -9
     * 命令 ps aux|grep redis 查看redis进程
     *
     *     root  4199  0.6  0.3 137448  7480 ?   Ssl  05:55   0:00 ./redis-server *:6379
     *
     * 关闭命令
     *      kill 4199既可关闭redis进程
     *
     * @2 第二种 （推荐使用）
     *
     * 命令 ./redis-cli shutdown
     * */

    /**
     * @五.Redis集群架构
     */
    /*
     * @1架构细节
     *
     * 1. 所有的redis节点彼此互联（PING-PONG机制）内部使用二进制协议优化传输速度和带宽
     *
     * 2. 节点的fail是通过集群中超过半数的节点检测失效时才生效
     *
     * 3. 客户端与redis节点直连,不需要中间proxy层,客户端不需要连接集群所有节点,连接集群中任何一个可用节点即可
     *
     * 4. redis-cluster把所有的物理节点映射到 slot上,Cluster负责维护
     *
     * 5. node->slot->value
     *
     * 6. Redis集群配置了16384个哈希槽,当需要在Redis集群中放置一个key-value时
     *
     * redis先对key使用crc16算法算出一个结果,然后把结果对16384求余数这样每个key
     *
     * 都会对应一个编号在0-16384之间的哈希槽redis会根据节点数量大致均等的将哈希槽映射到不同的节点
     * */

    /**
     * @六.Redis集群搭建需知
     */
    /*
     * 1 Redis集群中至少应该有三个节点,要保证集群高可用,需要每个节点有一个备份机
     *
     * 2 Redis集群至少需要6台服务器
     *
     * 3 搭建伪分布式可以使用一台虚拟机运行6个Redis实例,需要修改Redis端口号7001-7006
     */

    /**
     * @七Redis集群开始搭建
     */
    /*
     * @1 将已编译好的redis目录下的bin目录 (#注意:复制的是bin目录)复制6份
     *
     * <即上一步中编译完成最终放在 /usr/local/redis目录下的bin>
     *
     * @2 在/usr/local目录下创建一个redis-cluster目录
     *
     * 命令 mkdir redis-cluster
     *
     * @3 执行拷贝
     *
     * 命令 cp redis/bin redis-cluster/redis01 -r
     *
     * @4 进入redis01目录下如果有appendonly.apf dump.rdb请将它们删除,
     *
     * 保证这是一个干净的redis <如果没有请忽略>
     *
     *  命令 rm -f appendonly.aof  rm -f dump.rdb(#注意:此处不是删除目录,所以不需要使用 rm -rf)
     *
     * @5 更改配置文件
     *
     * 5.1 修改端口号
     *
     * 命令 vim redis.conf 修改 port 6379 更改为 port 7001
     *
     * 5.2
     *
     * 修改cluster-enabled yes
     *
     * (###注意:这段是注释上的通过 vim命令中文件检索 /cluster 检索到将注释去掉即可）
     *
     * @6 将redis01拷贝5份
     *
     * 命令 cp -r redis01/ redis02 ...
     *
     * @7 修改每个redis0x(x代表2 - 6) 下的端口号,这一步只需要修改端口号即可
     *
     * @8 创建批处理文件
     *
     * 8.1 一次全部启动（#注意批处理文件要创建在和redis0x同级目录）
     *
     * 命令 vim start-all.sh 将下列命令复制到start-all.sh中即可
     *
     * cd redis01
     * ./redis-server redis.conf
     * cd ..
     * cd redis02
     * ./redis-server redis.conf
     * cd ..
     * cd redis03
     * ./redis-server redis.conf
     * cd ..
     * cd redis04
     * ./redis-server redis.conf
     * cd ..
     * cd redis05
     * ./redis-server redis.conf
     * cd ..
     * cd redis06
     * ./redis-server redis.conf
     *
     * 8.2 一次全部关闭批处理
     *
     * 命令 vim end-all.sh 同样end-all.sh也要创建在和redis0x同级目录
     *
     * cd redis01
     * ./redis-cli shoutdown
     * cd ..
     * cd redis02
     * ./redis-cli shoutdown
     * cd ..
     * cd redis03
     * ./redis-cli shoutdown
     * cd ..
     * cd redis04
     * ./redis-cli shoutdown
     * cd ..
     * cd redis05
     * ./redis-cli shoutdown
     * cd ..
     * cd redis06
     * ./redis-cli shoutdown
     *
     * （###注意:批处理文件创建完成一般权限不足此处为了省事（##我只是为了省事，请根据实际情况赋予权限） chmod 777 start-all.sh  chmod 777 end-all.sh）
     *
     * @9 全部启动命令 ./start-all.sh 全部关闭命令 ./end-all.sh
     *
     * @10 将集群的6个节点连接到一起
     *
     * 10.1 连接集群需要一个工具,这个工具在redis源代码中（也就是第一步解压缩后的那个文件夹）
     *
     * 10.2 进入源代码目录
     *
     * 命令 cd redis-3.0.0
     *
     * 10.2 进入src目录
     *
     * 命令 cd src
     *
     * 10.3 找到.rb文件
     *
     * 命令 ll *.rb 会看到redis-trib.rb（这是一个ruby脚本）
     *
     * 10.4 拷贝这个脚本文件到redis目录
     *
     * 命令 cp redis-trib.rb /usr/local/redis-cluster
     *
     * 10.5 运行ruby脚本需要ruby环境
     *
     * 10.6 如果没有ruby可在线安装  （需要连接外网）
     *
     * 命令 yum install ruby  yum install rubygems
     *
     * 10.7 需要一个第三方库redis-3.0.0.gem
     *
     * 将文件传到linux系统
     *
     * 10.7.1 执行命令
     *
     * gem install redis-3.0.0
     *
     * 至此ruby相关已装好
     *
     * 10.8 进入redis-cluster目录下执行ruby脚本
     * ./redis-trib.rb create --replicas 1 192.168.25.128:7001 192.168.25.128:7002 192.168.25.128:7003 192.168.25.128:7004 192.168.25.128:7005 192.168.25.128:7006
     *
     * */
}
