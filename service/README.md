# 服务器

> 医院预约挂号平台与电子病历管理系统-服务器

## 依赖信息

本次的项目应该目前的条件，只有一个云服务器，并不适合使用微服务的框架，因此将会使用 `SpringBoot` 为基础进行开发。

目前的框架相关版本控制信息如下：

> 此处使用了 Spring Initializr 快速创建项目，自动控制大部分的依赖版本

- Java 1.8
- Spring 2.3.7.RELEASE
- Lombok 1.18.16
- MySQL Driver 8.0.22
- MyBatis Framework 2.1.4

## 项目结构

根据用例图，完成项目代码，并对代码的结构进行设计。

![服务器类图](./img/project_class_1.svg)

因为使用了MyBatis，利用其中的动态SQL语句特性，实现了动态查询的效果，因此基础的查询（`SELECT`）、更新（`UPDATE`）、插入（`INSERT`）和删除（`DELETE`）代码基本相同，为了减少其中的重复代码，抽出了 `GeneralMapperImpl` 的抽象类，在其中统一了CRUD操作，之后的Mapper只需要在此基础上进行扩展（`extends`）即可。

之后的部分与基础的代码框架基本一致，每个表格对应自己的Mapper，Mapper被特定的Service使用。项目中，主要的逻辑在Service中进行实现。最终通过Controller对外提供服务。

## 接口文档

其中一般遵循以下规则：

- 如果接口只接受一个参数，一般这个参数名就叫 `p`
- 查询数据的接口一般使用 `GET`
- 日期的格式都是 `yyyy-MM-dd HH:mm:ss`

### 用户信息

> `UserInfo` 中的数据 
>
> Long id; 
>
> String username;
>
> String password; 
>
> String passwordSalt; 
>
> String email; 
>
> Long phone; 
>
> Boolean isLogin; 
>
> LocalDateTime lastLoginTime; 
>
> String userImageUri;

| 登录接口 |                                                              |                                                              |
| -------- | ------------------------------------------------------------ | ------------------------------------------------------------ |
| 接口URI  | login                                                        |                                                              |
| 访问方式 | `POST`                                                       |                                                              |
| 参数     | `{ "username": username, "password": password }`             |                                                              |
| 返回值   | - 成功 <br>`{ code: 200, msg: "登录成功", data: UserInfo }` <br> - 失败 <br>`{ code: ExceptionCode, msg: 登录失败提示信息, data: null }` | ExceptionCode 中错误码：<br>9999: 数据库中没有找到用户的密码盐<br>3000: 数据库操作报错<br>3001: 用户操作过程中出现报错，一般是常规错误 |



| 注销接口 |                                                              |                                                              |
| -------- | ------------------------------------------------------------ | ------------------------------------------------------------ |
| 接口URI  | logout                                                       |                                                              |
| 访问方式 | `POST`                                                       |                                                              |
| 参数     | `{ "username": username }`                                   |                                                              |
| 返回值   | - 成功 <br/>`{ code: 200, msg: "注销成功", data: UserInfo }` <br/> - 失败 <br/>`{ code: ExceptionCode, msg: 注册失败信息, data: null }` | ExceptionCode 中错误码：<br/>3000: 数据库操作报错<br/>3001: 用户操作过程中出现报错，一般是常规错误 |



| 注册接口 |                                                              |                                                              |
| -------- | ------------------------------------------------------------ | ------------------------------------------------------------ |
| 接口URI  | register                                                     |                                                              |
| 访问方式 | `POST`                                                       |                                                              |
| 参数     | `{ "username": username, "password": password, "passwordSalt": passwordSalt, ... }` | 其中，用户名和密码必填，其他可选，最好不要输入lastLoginTime，由服务器自动生成。**可以通过返回值中返回的对象得到自动生成的id** |
| 返回值   | - 成功 <br/>`{ code: 200, msg: "注册成功", data: userInfo }` <br/> - 失败 <br/>`{ code: ExceptionCode, msg: 注册失败信息, data: null }` | ExceptionCode 中错误码：<br/>3000: 数据库操作报错<br/>3001: 用户操作过程中出现报错，一般是常规错误 |



| 用户查询 |                                                              |                                                              |
| -------- | ------------------------------------------------------------ | ------------------------------------------------------------ |
| 接口URI  | users                                                        |                                                              |
| 访问方式 | `GET`                                                        |                                                              |
| 参数     | `{ "p": { key: value, },"pageNum": pageNum, "pageSize": pageSize, "ordered": key, "desc": isDesc }` | - `p` 用于查找的Map，其中的key就是上方的`UserInfo` 的属性名<br>- `pageNum` 页号；`pageSize` 页大小（默认为显示10条数据）<br>- `ordered` 排序的键；`desc` 是否逆序（默认正序）<br />除了 `p` 其他数据都是可选 |
| 返回值   | - 成功 <br/>`{ code: 200, msg: 查询成功, data: [userInfo1, userInfo2, ] }` <br/> - 失败 <br/>`{ code: ExceptionCode, msg: 查询失败信息, data: null }` | ExceptionCode 中错误码：<br/>3000: 数据库操作报错<br/>3001: 用户操作过程中出现报错，一般是常规错误 |



| 用户更新 |                                                              |                                                              |
| -------- | ------------------------------------------------------------ | ------------------------------------------------------------ |
| 接口URI  | users/update                                                 |                                                              |
| 访问方式 | `POST`                                                       |                                                              |
| 参数     | `{ "before": { key: value, }, "after": { key: value, } }`    | - `before` 表示用于寻找的 Map，筛选需要更新的用户<br>- `after` 表示修改的新的键值对 |
| 返回值   | - 成功 <br/>`{ code: 200, msg: 更新成功, data: 更新成功的数量 }` <br/> - 失败 <br/>`{ code: ExceptionCode, msg: 更新失败信息, data: null }` | ExceptionCode 中错误码：<br/>3000: 数据库操作报错<br/>3001: 用户操作过程中出现报错，一般是常规错误 |



| 用户添加 |                                                              |                                                              |
| -------- | ------------------------------------------------------------ | ------------------------------------------------------------ |
| 接口URI  | users/insert                                                 |                                                              |
| 访问方式 | `POST`                                                       |                                                              |
| 参数     | `[ {"username": username, "password": password, "passwordSalt": passwordSalt, ...}, {"username": username, "password": password, "passwordSalt": passwordSalt, ...}, ]` | 用户名和密码必填，其他可选，一般不传id，使用数据库自动生成，最好不要输入lastLoginTime，由服务器自动生成。**可以通过返回值中得到的对象来获取到自动生成的id** |
| 返回值   | - 成功 <br/>`{ code: 200, msg: 添加成功, data: {"count": 添加成功的数量, "result": 初始化之后的对象列表} }` <br/> - 失败 <br/>`{ code: ExceptionCode, msg: 添加失败信息, data: null }` | ExceptionCode 中错误码：<br/>3000: 数据库操作报错<br/>3001: 用户操作过程中出现报错，一般是常规错误 |



| 用户删除 |                                                              |                                                              |
| -------- | ------------------------------------------------------------ | ------------------------------------------------------------ |
| 接口URI  | users/delete                                                 |                                                              |
| 访问方式 | `POST`                                                       |                                                              |
| 参数     | `{ key: value, }`                                            | key 对应上方的 `UserInfo` 的属性名<br />如，需要删除的是 `username` 等于 test1 此处就传入<br />`{ "username": "test1" }` |
| 返回值   | - 成功 <br/>`{ code: 200, msg: 删除成功, data: 删除成功的数量 }` <br/> - 失败 <br/>`{ code: ExceptionCode, msg: 删除失败信息, data: null }` | ExceptionCode 中错误码：<br/>3000: 数据库操作报错<br/>3001: 用户操作过程中出现报错，一般是常规错误 |

### 科室信息

> `DepartmentInfo` 中的数据
>
> Long id;
> Long hospitalId; 
> String departmentName;
>
> hospitalId 暂时不影响，基本上没有用，如果之后需要可以直接支持，目前默认为0，可以不传

| 科室查找 |                                                              |                                                              |
| -------- | ------------------------------------------------------------ | ------------------------------------------------------------ |
| 接口URI  | departments                                                  |                                                              |
| 访问方式 | `GET`                                                        |                                                              |
| 参数     | `{ "p": { key: value, },"pageNum": pageNum, "pageSize": pageSize, "ordered": key, "desc": isDesc }` | - `p` 用于查找的Map，其中的key就是上方的`DepartmentInfo` 的属性名<br/>- `pageNum` 页号；`pageSize` 页大小（默认为显示10条数据）<br/>- `ordered` 排序的键；`desc` 是否逆序（默认正序）<br />除了 `p` 其他数据都是可选 |
| 返回值   | - 成功 <br/>`{ code: 200, msg: 查询成功, data: [departmentInfo1, departmentInfo2, ] }` <br/> - 失败 <br/>`{ code: ExceptionCode, msg: 查询失败信息, data: null }` | ExceptionCode 中错误码：<br/>3000: 数据库操作报错<br/>3003: 科室数据操作过程中出现报错，一般是常规错误 |



| 科室更新 |                                                              |                                                              |
| -------- | ------------------------------------------------------------ | ------------------------------------------------------------ |
| 接口URI  | departments/update                                           |                                                              |
| 访问方式 | `POST`                                                       |                                                              |
| 参数     | `{ "before": { key: value, }, "after": { key: value, } }`    | - `before` 表示用于寻找的 Map，筛选需要更新的科室<br>- `after` 表示修改的新的键值对 |
| 返回值   | - 成功 <br/>`{ code: 200, msg: 更新成功, data: 更新成功的数量 }` <br/> - 失败 <br/>`{ code: ExceptionCode, msg: 更新失败信息, data: null }` | ExceptionCode 中错误码：<br/>3000: 数据库操作报错<br/>3003: 科室数据操作过程中出现报错，一般是常规错误 |



| 科室添加 |                                                              |                                                              |
| -------- | ------------------------------------------------------------ | ------------------------------------------------------------ |
| 接口URI  | departments/insert                                           |                                                              |
| 访问方式 | `POST`                                                       |                                                              |
| 参数     | `[ {"id": id, "hospitalId": hospitalId, "departmentName": departmentName}, ... ]` | 所有数据可选，一般不传id，使用数据库自动生成。**可以通过返回值中返回的对象来得到自动生成的id** |
| 返回值   | - 成功 <br/>`{ code: 200, msg: 添加成功, data: {"count": 添加成功的数量, "result": 初始化之后的对象列表} }` <br/> - 失败 <br/>`{ code: ExceptionCode, msg: 添加失败信息, data: null }` | ExceptionCode 中错误码：<br/>3000: 数据库操作报错<br/>3003: 科室数据操作过程中出现报错，一般是常规错误 |



| 科室删除 |                                                              |                                                              |
| -------- | ------------------------------------------------------------ | ------------------------------------------------------------ |
| 接口URI  | departments/delete                                           |                                                              |
| 访问方式 | `POST`                                                       |                                                              |
| 参数     | `{ key: value, }`                                            | key 对应上方的 `DepartmentInfo` 的属性名<br />如，查询“口腔科”，则 `{ "departmentName": "口腔科" }` |
| 返回值   | - 成功 <br/>`{ code: 200, msg: 删除成功, data: 删除的数量 }` <br/> - 失败 <br/>`{ code: ExceptionCode, msg: 删除失败信息, data: null }` | ExceptionCode 中错误码：<br/>3000: 数据库操作报错<br/>3003: 科室数据操作过程中出现报错，一般是常规错误 |

### 医生信息

> `DoctorInfo` 中的数据
>
> Long id;
> Long departmentId;
> String doctorName;
> Float reservationPrice;
> Integer remainingCapacity;
> String doctorImageUri;

| 医生查找 |                                                              |                                                              |
| -------- | ------------------------------------------------------------ | ------------------------------------------------------------ |
| 接口URI  | doctors                                                      |                                                              |
| 访问方式 | `GET`                                                        |                                                              |
| 参数     | `{ "p": { key: value, },"pageNum": pageNum, "pageSize": pageSize, "ordered": key, "desc": isDesc }` | - `p` 用于查找的Map，其中的key就是上方的`DepartmentInfo` 的属性名<br/>- `pageNum` 页号；`pageSize` 页大小（默认显示10条数据）<br/>- `ordered` 排序的键；`desc` 是否逆序（默认正序）<br />除了 `p` 其他数据都是可选 |
| 返回值   | - 成功 <br/>`{ code: 200, msg: 查询成功, data: [doctorInfo1, doctorInfo2, ] }` <br/> - 失败 <br/>`{ code: ExceptionCode, msg: 查询失败信息, data: null }` | ExceptionCode 中错误码：<br/>3000: 数据库操作报错<br/>3004: 医生数据操作过程中出现报错，一般是常规错误 |



| 科室更新 |                                                              |                                                              |
| -------- | ------------------------------------------------------------ | ------------------------------------------------------------ |
| 接口URI  | doctors/update                                               |                                                              |
| 访问方式 | `POST`                                                       |                                                              |
| 参数     | `{ "before": { key: value, }, "after": { key: value, } }`    | - `before` 表示用于寻找的 Map，筛选需要更新的科室<br>- `after` 表示修改的新的键值对 |
| 返回值   | - 成功 <br/>`{ code: 200, msg: 更新成功, data: 更新成功的数量 }` <br/> - 失败 <br/>`{ code: ExceptionCode, msg: 更新失败信息, data: null }` | ExceptionCode 中错误码：<br/>3000: 数据库操作报错<br/>3004: 医生数据操作过程中出现报错，一般是常规错误 |



| 科室添加 |                                                              |                                                              |
| -------- | ------------------------------------------------------------ | ------------------------------------------------------------ |
| 接口URI  | doctors/insert                                               |                                                              |
| 访问方式 | `POST`                                                       |                                                              |
| 参数     | `[ {"id": id, "departmentId": departmentId, "doctorName": doctorName, "reservationPrice": reservationPrice, ...}, ... ] ` | 其中，科室id必填（医生姓名不填不会报错，但是从逻辑上讲应该也是必填），其他可选，id最好由服务器自动生成（**在返回值中，可以得到新建对象的id**） |
| 返回值   | - 成功 <br/>`{ code: 200, msg: 添加成功, data: {"count": 添加成功的数量, "result": 经过初始化后的对象列表} }` <br/> - 失败 <br/>`{ code: ExceptionCode, msg: 添加失败信息, data: null }` | ExceptionCode 中错误码：<br/>3000: 数据库操作报错<br/>3004: 医生数据操作过程中出现报错，一般是常规错误 |



| 科室删除 |                                                              |                                                              |
| -------- | ------------------------------------------------------------ | ------------------------------------------------------------ |
| 接口URI  | doctors/delete                                               |                                                              |
| 访问方式 | `POST`                                                       |                                                              |
| 参数     | `{ key: value, }`                                            | key 对应上方的 `DoctorInfo` 的属性名<br />如，需要查询医生小明，则 `{"doctorName": "小明"}` |
| 返回值   | - 成功 <br/>`{ code: 200, msg: 删除成功, data: 删除的数量 }` <br/> - 失败 <br/>`{ code: ExceptionCode, msg: 删除失败信息, data: null }` | ExceptionCode 中错误码：<br/>3000: 数据库操作报错<br/>3004: 医生操作过程中出现报错，一般是常规错误 |

### 预约接口

| 预约     |                                                              |                                                              |
| -------- | ------------------------------------------------------------ | ------------------------------------------------------------ |
| 接口URI  | reservation                                                  |                                                              |
| 访问方式 | `POST`                                                       |                                                              |
| 参数     | `{ "userId": userId, "doctorId": doctorId, "date": date }`   | - `date` 表示预约的时间，使用 `yyyy-MM-dd HH:mm:ss` 格式     |
| 返回值   | - 成功 <br/>`{ code: 200, msg: 预约成功, data: 预约订单号 }` <br/> - 失败 <br/>`{ code: ExceptionCode, msg: 预约失败信息, data: null }` | ExceptionCode 中错误码：<br/>3000: 数据库操作报错<br/>3001: 用户查询出现问题，一般是常规错误<br />3004: 医生查询出现问题，一般是常规错误<br />4000: 医生操作过程中出现报错，一般是常规错误 |



| 查询剩余容量 |                                                              |                                                              |
| ------------ | ------------------------------------------------------------ | ------------------------------------------------------------ |
| 接口URI      | capacity                                                     |                                                              |
| 访问方式     | `POST`                                                       |                                                              |
| 参数         | `{ doctorId: doctorId, date: date }`                         | - `date` 表示预约的时间，使用 `yyyy-MM-dd HH:mm:ss` 格式     |
| 返回值       | - 成功 <br/>`{ code: 200, msg: 查询成功, data: 剩余容量 }` <br/> - 失败 <br/>`{ code: ExceptionCode, msg: 查询失败信息, date: null }` | ExceptionCode 中错误码：<br/>3000: 数据库操作报错<br/>3004: 医生查询出现问题，一般是常规错误<br />4000: 医生操作过程中出现报错，一般是常规错误 |

