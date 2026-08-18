scoreboard objectives add powah_gallery dummy
function powah_gallery:clear
fill 199 99 199 229 99 217 minecraft:smooth_stone

# Formed nitro reactor: command placement bypasses Powah's onPlaced hook, so
# seed its persisted build queue and let Powah create every part itself.
setblock 204 100 205 powah:reactor_nitro[core=true]
data merge block 204 100 205 {built:0b,queue_pos:[{Pos:[I;203,100,204]},{Pos:[I;203,100,205]},{Pos:[I;203,100,206]},{Pos:[I;204,100,204]},{Pos:[I;204,100,206]},{Pos:[I;205,100,204]},{Pos:[I;205,100,205]},{Pos:[I;205,100,206]},{Pos:[I;203,101,204]},{Pos:[I;203,101,205]},{Pos:[I;203,101,206]},{Pos:[I;204,101,204]},{Pos:[I;204,101,205]},{Pos:[I;204,101,206]},{Pos:[I;205,101,204]},{Pos:[I;205,101,205]},{Pos:[I;205,101,206]},{Pos:[I;203,102,204]},{Pos:[I;203,102,205]},{Pos:[I;203,102,206]},{Pos:[I;204,102,204]},{Pos:[I;204,102,205]},{Pos:[I;204,102,206]},{Pos:[I;205,102,204]},{Pos:[I;205,102,205]},{Pos:[I;205,102,206]},{Pos:[I;203,103,204]},{Pos:[I;203,103,205]},{Pos:[I;203,103,206]},{Pos:[I;204,103,204]},{Pos:[I;204,103,205]},{Pos:[I;204,103,206]},{Pos:[I;205,103,204]},{Pos:[I;205,103,205]},{Pos:[I;205,103,206]}]}

# Loose unformed reactor control.
setblock 210 100 205 powah:reactor_basic[core=true]

# Cable T: north=ALL, south=EXTRACT, west=RECEIVE, east=same-tier cable.
setblock 216 100 205 minecraft:smooth_stone
setblock 216 101 204 powah:energy_cell_basic
setblock 216 101 206 powah:energy_cell_basic
setblock 215 101 205 powah:energy_cell_basic
setblock 217 101 205 powah:energy_cable_basic[down=false,east=true,north=false,south=false,up=false,west=true]
setblock 216 101 205 powah:energy_cable_basic[down=false,east=true,north=true,south=true,up=false,west=true]
data merge block 216 101 205 {cs:28b,side_transfer_type:[I;0,0,0,1,2,0]}

# The charge cube is stable; displayed energizing items are intentionally absent.
setblock 223 100 204 powah:energizing_orb[facing=down]
setblock 224 101 209 minecraft:smooth_stone
setblock 223 101 209 powah:energizing_orb[facing=east]

# Stock JSON controls that require no add-on route.
setblock 228 100 205 powah:solar_panel_basic
setblock 228 100 209 powah:energy_cell_basic

scoreboard players set #ready powah_gallery 1
tellraw @a [{"text":"Powah BlueMap gallery built inside x199..229, z199..217.","color":"aqua"}]
