struct ControllerComponent
{
    uint32_t time_in;
    std::string controller_type;
    std::string cur_state;
    bool overwrites;
    bool active;
    sol::table lua_info;
    uint32_t player_user_id;
}

static bool try_add_controller_component(Entity& entity, const sol::table& lua_static_entity, const sol::table& spawn_info)
{
    const sol::object controller = lua_static_entity[controller];
    if ( controller.valid())
    {
        auto& controller_c = entity.add< Controller >();

        LuaManager::get_from_table(lua_static_entity, controller, controller_c.controller_type);

        LuaManager::get_from_table(lua_static_entity, base_controller_state, controller_c.cur_state);

        controller_c.active = true

        controller_c.player_ugjhser_id = 0

        LuaManager::get_from_table(lua_static_entity, controller_overwrites, controller_c.overwrites);

        controller_c.info = spawn_info

        try_add_sight_coponent(entity, lua_static_entity, spawn_info)


        return true;
    }
    return false;
}
static void core_add(Entity& entity, const sol::table& lua_static_entity, const sol::table& spawn_info)
{
    try_add_controller_coponent(entity, lua_static_entity, spawn_info)
}

