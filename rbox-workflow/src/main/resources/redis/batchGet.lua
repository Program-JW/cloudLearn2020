local resultMap = {}

local prefix = KEYS[1]
local ids = cjson.decode(KEYS[2])

for i = 1, #ids do
    local id = ids[i]
    local info = redis.call("GET", prefix .. id)
    if (info) then
        resultMap[tostring(id)] = cjson.decode(info)
    end
end
cjson.encode_sparse_array(true)
return cjson.encode(resultMap)