# 工程简介

# 延伸阅读

# 注意事项

数据库字符集编码不要使用utf8mb4

因为如果你设置的一个varchar字段使用的是utf8mb4字符集，那么索引最多可以存储 767 / 4 ＝ 191个这样的字符。因为utf8mb4的字符每个占4B的存储空间，如果varchar使用utf8字符集，那么索引最多可以存储767 / 3 = 254个这样的字符。因为使用utf8存储的字符占3B的存储空间

否则会出现Specified key was too long; max key length is 767 bytes这个问题

因为flowable的id是varchar，然后又根据id建索引，所以如果没有足够的空间就会报错
