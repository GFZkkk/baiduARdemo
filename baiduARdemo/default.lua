app_controller = ae.ARApplicationController:shared_instance()
app_controller:require('./scripts/include.lua')
app = AR:create_application(AppType.ImageTrack, 'bear')
app:load_scene_from_json('res/main.json','demo_scene')
scene = app:get_current_scene()

app.on_loading_finish = function()
	app:onArInit()
	EDLOG('资源包加载完成后回调,相当于主函数')

end

local stop = function (node,node1)
	ARLOG('myanim')
	node:set_visible(false)
	node1:set_visible(true)
 	node1:pod_anim():repeat_count(1):start()
end

local anim = function(node,node1)
	stop(node,node1)
	--node:pod_anim()
	--		:repeat_count(1)
	--		:frame_start()
	--		:frame_end()
	--		:on_complete(stop(node,node1))
	--		:start()
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
	--scene.bear:set_scale(x1,y1,z1)
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

local rotate = function (x3,y3,z3)
    --local time = 800
    --if s ~= nil then
    --    time = s
    --end
    --scene.bear:rotate_by()
    --     :duration(time)
    --     :axis_xyz(Vector3(x3,y3,z3))
    --     :to_degree(360)
    --     :start()
	scene.bear:set_rotation_by_xyz(x3,y3,z3)
end



--scene.blackOff.on_click =function()
--	anim(scene.blackOff,scene.blackOn)
--end
--
--scene.blackOn.on_click =function()
--	anim(scene.blackOn,scene.blackOff)
--end

scene.bear_blo.on_click= function()
    anim(scene.blo,scene.blc)
    anim(scene.bro,scene.brc)
end

scene.bear_bro.on_click= function()
    anim(scene.blo,scene.blc)
    anim(scene.bro,scene.brc)
end

scene.bear_blc.on_click= function()
	anim(scene.blc,scene.blo)
    anim(scene.brc,scene.bro)
end

scene.bear_brc.on_click= function()
	anim(scene.blc,scene.blo)
    anim(scene.brc,scene.bro)
end

scene.bear_box11Off.on_click = function()
	anim(scene.box11Off,scene.box11On)
    ARLOG('box11Off')
    --scene.box11Off:pod_anim():repeat_count(1):start()
end

scene.bear_box11On.on_click = function()
	anim(scene.box11On,scene.box11Off)
    ARLOG('box11On')
    --scene.box11On:pod_anim():repeat_count(1):start()
end

scene.bear_box12On.on_click = function()
	scene.zng:set_visible(true)
end

scene.zng.on_click = function()
	scene.zng:set_visible(false)
end

local flag = false
local key = 1
scene.right_1.on_click = function()
	if reset == false then
		scale(2*key,2*key,2*key)
	    move(0,0,300)
		reset = true
	elseif reset == true then
		scale(0.5*key,0.5*key,0.5*key)
	    move(0,0,-300)
		reset = false
	end
end

local initmove = {0,800,300}
-- app.onArInit为编辑工具自动生成的方法，请勿删除/修改方法內的内容
app.onArInit = function(self)
	-- 框架方法，请勿删除或修改
	move(initmove[1],initmove[2],initmove[3])
end

local first = false

local reset = function()
	app:relocate_current_scene()
	if first == true then
		move(initmove[1],initmove[2],initmove[3])
		first = false
	else
		move(-1 * initmove[1],-1 * initmove[2],-1 * initmove[3])
		first = true
	end
end

app.on_target_lost = function()
	ARLOG('on target lost')
	app.device:open_imu(1)
	--rotate(90,0,0)
	if first == true then
		move(initmove[1],initmove[2],initmove[3])
		first = false
	end
end

app.on_target_found = function()
    ARLOG('on_target_found')
    app.device:open_imu(0)
    if first == false then
		move(-1 * initmove[1],-1 * initmove[2],-1 * initmove[3])
		first = true
    end
end




