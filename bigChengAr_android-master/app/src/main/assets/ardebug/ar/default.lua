app_controller = ae.ARApplicationController:shared_instance()
app_controller:require('./scripts/include.lua')
app = AR:create_application(AppType.ImageTrack, 'bear')
app:load_scene_from_json('res/main.json','demo_scene')
scene = app:get_current_scene()

app.on_loading_finish = function()
    app:onArInit()
    EDLOG('资源包加载完成后回调,相当于主函数')
    app:open_url("loadFinish")
end

local scale = function (x1,y1,z1,s)
    local time = 800
    if s ~= nil then
        time = s
    end
    scene.bear:scale_by()
         :duration(time)
         :to(Vector3(x1,y1,z1))
         :start()
    --scene.room:set_scale(x1,y1,z1)
end

local move = function(x2,y2,z2,s)
    local time = 0
    if s ~= nil then
        time = s
    end
    scene.bear:move_by()
         :duration(time)
         :to(Vector3(x2,y2,z2))
         :start()
end

local rotate1 = function (x4,y4,z4)
    --local time = 800
    --if s ~= nil then
    --    time = s
    --end
    --scene.room:rotate_by()
    --     :duration(time)
    --     :axis_xyz(Vector3(x3,y3,z3))
    --     :to_degree(360)
    --     :start()
end

local rotate = function (x3,y3,z3)
    scene.bear:set_rotation_by_xyz(x3,y3,z3)
end

local anim = function(node)
    node:pod_anim():repeat_count(1):start()
end

local ImageTrack = false

local initmove = {0,0,-1600}



local zoom = false
local key = 1
app.on_target_lost = function()
    ARLOG('on target lost')
    app.device:open_imu(1,1)
    --app:set_camera_look_at("0, 0, 1400","0, 0, 0", "0.0, 1.0, 0.0",true)
    --scene.simplepod:set_rotation_by_xyz(0, -50, -35)
    if ImageTrack == true then
        move(initmove[1],initmove[2],initmove[3])
        rotate(0,-30,-90)
        ImageTrack = false
    end
--[[    if zoom == false then
        scale(2,2,2)
        zoom = true
    end]]
end

app.on_target_found = function()
    ARLOG('on taeget found')
    app.device:close_imu()
    --scene.simplepod:set_rotation_by_xyz(90, 0, 0)
    if ImageTrack == false then
        move(-1 * initmove[1],-1 * initmove[2],-1 * initmove[3])
        rotate(90,0,0)

        ImageTrack = true
    end
--[[    if zoom == true then
        scale(0.5,0.5,0.5)
        zoom = false
    end]]
end



-- app.onArInit为编辑工具自动生成的方法，请勿删除/修改方法內的内容
app.onArInit = function(self)
    --scene.root:audio():path('res/audio/10001198/bg.mp3')
    -- 框架方法，请勿删除或修改
    move(initmove[1],initmove[2],initmove[3])
end

scene.reset.on_click = function()
    app:relocate_current_scene()
    app.device:close_imu()
    app.device:open_imu(1,1)
    if ImageTrack == false then
        move(initmove[1],initmove[2],initmove[3])
        rotate(0,-30,-90)
        --scale(2,2,2)
    end
end

scene.black.on_click=function()
    app:open_url('black')
end

scene.left.on_click=function()
    app:open_url('left')
end

scene.right.on_click=function()
    app:open_url('right')
end

scene.zng.on_click=function()
    app:open_url('zng')
end
scene.gift.on_click=function()
    app:open_url('gift')
end


