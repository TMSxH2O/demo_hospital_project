# 数据库

该该项目中将涉及到的核心的两个功能：

1. 医院预约
2. 查询个人病例

> 其中，医院预约可能涉及到支付功能，但是支付希望设计接入支付宝接口，因此不需要存储个人的余额。

大致将其中需要的内容分为以下几个部分：

- 用户基础个人信息（用户名、密码、邮箱等通常信息）
- 医院信息，主要对应预约相关（医院科室、科室医生、目前还剩多少名额等）
- 用户历史信息，包括历史病例和历史预约

## 更新记录

- 2021-03-28 10:43:17 TMS_H2O

  创建文件，进行第一版的数据库设计

  - [ ] 确认数据库设计是否合理（确定之后可以点一下左边的标记）
  
- 2021-04-04 13:25:00 TMS_H2O

  根据现有的实现，生成对应的sql文件

## 数据库设计

> 按照最上方的分析进行设计

### 用户基础个人信息

> 表名：user_info

| 字段名               | 字段类型 | 字段大小 | 非空     | 默认值                | 注释                                               |
| -------------------- | -------- | -------- | -------- | --------------------- | :------------------------------------------------- |
| username             | char     | 20       | not null | ""                    | 用户名，不可重复，登录时使用（担任id的功能）       |
| user_pwd             | char     | 32       | not null | ""                    | 用户密码，MD5加密，登录时使用                      |
| user_pwd_salt        | char     | 32       | not null | ""                    | 用户密码加密过程中产生的盐，用于密码校验           |
| user_email           | char     | 40       | not null | ""                    | 用户的邮箱，可以用于找回密码（可能不做这个功能）   |
| user_phone           | bigint   | 11       | not null | 0                     | 用户的手机号，可以用于找回密码（可能不做这个功能） |
| user_is_login        | tinyint  | 1        | not null | 0                     | 用户是否登录，0表示未登录，1表示已登录             |
| user_last_login_time | datetime | 1        | not null | 1000-01-01 00:00:00.0 | 用户最后登录时间（可能不需要）                     |
| user_image_uri       | char     | 50       | not null | ""                    | 用户头像图片路径（相对路径）                       |

### 医院信息

> 表名：hospital_info

| 字段名           | 字段类型 | 字段大小 | 非空     | 默认值         | 注释                                     |
| ---------------- | -------- | -------- | -------- | -------------- | :--------------------------------------- |
| hospital_id      | bigint   | 20       | not null | auto_increment | 医院id，用于科室指定医院                 |
| hospital_name    | char     | 20       | not null | ""             | 医院名，不可重复，用于与医院信息进行映射 |
| hospital_address | char     | 50       | not null | ""             | 医院位置信息                             |
| hospital_phone   | bigint   | 11       | not null | 0              | 医院联系电话                             |

> 表名：department_info

| 字段名          | 字段类型 | 字段大小 | 非空     | 默认值         | 注释                         |
| --------------- | -------- | -------- | -------- | -------------- | ---------------------------- |
| department_id   | bigint   | 20       | not null | auto_increment | 医院科室id                   |
| hospital_id     | bigint   | 20       | not null | 0              | 医院id，使科室链接指定的医院 |
| department_name | char     | 30       | not null | ""             | 科室名                       |

> 表名：doctor_info

| 字段名             | 字段类型     | 字段大小 | 非空     | 默认值         | 注释                           |
| ------------------ | ------------ | -------- | -------- | -------------- | ------------------------------ |
| doctor_id          | bigint       | 20       | not null | auto_increment | 医生的编号id                   |
| department_id      | bigint       | 20       | not null | 0              | 科室id，指定该医生对应哪个科室 |
| doctor_name        | char         | 20       | not null | ""             | 医生姓名                       |
| reservation_price  | float        | (10, 2)  | not null | 0.00           | 预约价格                       |
| remaining_capacity | unsigned int | 10       | not null | 0              | 医生当前剩余容量               |
| doctor_image_uri   | char         | 50       | not null | ""             | 医生照片路径（相对路径）       |

### 用户历史信息

> 表名：user_hospital_reservation_info

| 字段名             | 字段类型 | 字段大小 | 非空     | 默认值 | 注释                                                  |
| ------------------ | -------- | -------- | -------- | ------ | ----------------------------------------------------- |
| reservation_id     | char     | 32       | not null | ""     | 唯一预约单号                                          |
| user_id            | bigint   | 20       | not null | 0      | 用户id，预约信息必须与某个用户链接才有意义            |
| doctor_id          | bigint   | 20       | not null | 0      | 医生id，预约信息必须与某个医生链接才有意义            |
| reservation_date   | date     | 1        | not null |        | 预约日期                                              |
| reservation_price  | float    | (10, 2)  | not null | 0.0    | 预约价格                                              |
| reservation_status | tinyint  | 1        | not null | 0      | 订单状态，0未支付，1已支付，2已报道，3已处理，9已取消 |

> 表名：user_medical_history

| 字段名               | 字段类型 | 字段大小 | 非空     | 默认hi         | 注释                                       |
| -------------------- | -------- | -------- | -------- | -------------- | ------------------------------------------ |
| medical history_id   | bigint   | 20       | not null | auto_increment | 病例id                                     |
| user_id              | bigint   | 20       | not null | 0              | 用户id，病例信息必须与某个用户链接才有意义 |
| doctor_id            | bigint   | 20       | not null | 0              | 医生id，病例信息必须与某个医生链接才有意义 |
| medical history_date | date     | 1        | not null |                | 病例对应的时间                             |
| medical history_uri  | char     | 50       | not null | ""             | 病例文件对应的位置（相对路径）             |

