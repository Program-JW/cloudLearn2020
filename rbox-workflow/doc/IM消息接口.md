### 1.创建群
创建群，可以同时指定若干群成员

#### 请求
```json
{
  "action":"",
  "param": {
    "appId":"im服务分配的应用标识符，必填",
    "name":"群名称，必填",
    "members":[
      {
        "code":"应用用户体系中的用户唯一标识符，必填",
        "nickName":"成员昵称，必填"
      }
    ]
  }
}

```
#### 响应
```json
{
  "code":"结果码，数字",
  "data":[
    {
      "id":"群成员标识符",
      "code":"应用用户体系中的用户唯一标识符，必填"
    }
  ],
  "message":"错误详细信息"
}
```

### 2.增加群成员
增加群成员,且可以同时指定若干群成员

#### 请求
```json
{
  "action":"",
  "param":{
    "id":"群ID，必填",
    "appId":"im服务分配的应用标识符，必填",
    "members":[
      {
        "code":"应用用户体系中的用户唯一标识符，必填",
        "nickName":"成员昵称，必填"
      }
    ]
  }
}
```
#### 响应
```json
{
  "code":"结果码，数字",
  "message":"错误详细信息"
}
```

### 3.删除群成员
删除群成员

#### 请求
```json
{
  "action":"",
  "param":{
    "id":"群ID，必填",
    "appId":"im服务分配的应用标识符，必填",
    "members":[
      {
        "code":"应用用户体系中的用户唯一标识符，必填"
      }
    ]
  }
}
```
#### 响应
```json
{
  "code":"结果码，数字",
  "message":"错误详细信息"
}
```

### 4.删除群
删除群

#### 请求
```json
{
  "action":"",
  "param":{
    "id":"群ID，必填",
    "appId":"im服务分配的应用标识符，必填"
  }
}

```
#### 响应
```json
{
  "code":"结果码，数字",
  "message":"错误详细信息"
}
```

### 5.更新群

#### 请求
```json
{
  "action":"",
  "param":{
    "id":"群ID，必填",
    "name": "群名称"
  }
}
```
#### 响应
```json
{
  "code":"结果码，数字",
  "message":"错误详细信息"
}
```

### 6.登录

#### 请求
```json
{
  "action":"",
  "param":{
    "appId":"im服务分配的应用标识符，必填",
    "appSecretKey":"IM服务分配的应用秘钥",
    "username":"用户名",
    "platformId":"平台"
  }
}

```
#### 响应
```json
{
  "code":"结果码，数字",
  "data":[
    {
      "clientId":"客户端ID"
    }
  ],
  "message":"错误详细信息"
}
```

### 7.发消息

#### 请求
```json
{
  "action":"",
  "content":"消息内容"
}
```

#### 响应
```json
{
  "code":"结果码，数字",
  "message":"错误详细信息"
}
```

### 9.获取历史消息
startIndex如果不传，则表示所有消息

#### 请求
```json
{
  "action":"",
  "param":{
    "startIndex": ""
  }
}
```

#### 响应
```json
{
  "code":"结果码，数字",
  "data":[
    {"type": "","content": ""}
  ],
  "message":"错误详细信息"
}
```

### 10.推送
startIndex如果不传，则表示所有消息
消息体
```json
{
  "action":"",
  "content":""
}
```

