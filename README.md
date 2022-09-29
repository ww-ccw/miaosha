# 功能简介

1. 登录
    - 两次加盐，数据库中不存放明文密码
    - 生成token，并以token为key将用户信息存放入redis
    - 配置有参数分析器，如果controller需要user用户信息，分析器会在controller执行前通过token得到用户信息，并返回给controller
2. 得到秒杀商品列表
    - 从数据库中取出商品列表
3. 得到商品详情
    - 得到商品详情
4. 秒杀-下订单
    - 先检查库存->从redis中查找是否已经秒杀过->减库存、下订单、写入秒杀订单

# 网络传输优化

## 页面缓存

将商品列表、商品详情的通过手动渲染成为html页面存入redis,以避免每次查询都经过数据库和模块渲染，提高吞吐量

商品列表："/goods/to_list"	--->	cacheList()

商品详情："/goods/detail/{goodsId}"	--->	cacheDetail()

## 页面静态化---浏览器缓存

将商品详情、订单详情静态化、这样客户端的htm模板只需要加载一次，请求后浏览器就会缓存对应的htm模板。每次请求不一样的商品详情和订单详情时就**只需要传递相关参数**就行，减少网络传输。

商品详情："/goods/detail/{goodsId}"	--->	staticDetail()

订单详情：从goods_detail.htm的v2	--->staticMiaoSha()	--->跳转到order_detail.htm	--->ajax请求	"/order/detail"

## 防止超卖

1. 在减库存的时候加where只有库存大于0才减库存
2. 为在订单表为User和goodsID加上唯一索引

# 接口优化

目的：减少对数据库的查询，能缓存尽量缓存。同时对redis的访问也要尽量减少网络传输

## 减少对数据库的访问

1. 系统初始化，把商品库存数量加载到Redis中
2. 收到请求，Redis预减库存，库存不足直接返回。否则进入3
3. 请求入队，立即返回排队中
4. 请求出队，生成队列，减少库存
5. 客户端轮询是否秒杀成功

# 安全优化

1. 接口隐藏，为每个用户对每个商品的生成秒杀地址path
2. 秒杀时先请求秒杀地址，然后带上数学公式验证码去请求秒杀
3. 限流
    - 令牌桶
    - 漏桶
    - 计数器
        - 缺点：临界点问题、资源浪费