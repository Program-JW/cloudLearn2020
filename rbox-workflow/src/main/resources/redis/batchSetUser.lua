local prefix = KEYS[1]
local userInfoList = cjson.decode(KEYS[2])

for i = 1, #userInfoList do
    local key = prefix .. userInfoList[i]['userId']
    local info = userInfoList[i]['info']
    redis.call("SET", key, info)
    local timeout = 2592000 + math.random(172800)
    redis.call("EXPIRE", key, timeout)
end
