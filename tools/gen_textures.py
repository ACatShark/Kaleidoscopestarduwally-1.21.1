#!/usr/bin/env python3
# 生成新增作物所需的占位贴图(PNG)与全部 JSON 资源(方块状态/模型/战利品表/语言文件)。
# 仅依赖 Python 标准库，无需 Pillow。
import os, json, zlib, struct

ROOT = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
ASSETS = os.path.join(ROOT, "src", "main", "resources", "assets", "kaleidoscope_starduwally")
DATA = os.path.join(ROOT, "src", "main", "resources", "data", "kaleidoscope_starduwally")
MODID = "kaleidoscope_starduwally"
W = 16

# ---------------- PNG 编码 ----------------
def write_png(path, px):
    raw = bytearray()
    for row in px:
        raw.append(0)
        for (r, g, b, a) in row:
            raw.extend((r & 0xFF, g & 0xFF, b & 0xFF, a & 0xFF))
    comp = zlib.compress(bytes(raw), 9)
    def chunk(typ, data):
        return (struct.pack(">I", len(data)) + typ + data
                + struct.pack(">I", zlib.crc32(typ + data) & 0xFFFFFFFF))
    png = b"\x89PNG\r\n\x1a\n"
    png += chunk(b"IHDR", struct.pack(">IIBBBBB", W, W, 8, 6, 0, 0, 0))
    png += chunk(b"IDAT", comp)
    png += chunk(b"IEND", b"")
    with open(path, "wb") as f:
        f.write(png)

# ---------------- 画布/绘制工具 ----------------
def new_canvas():
    return [[(0, 0, 0, 0) for _ in range(W)] for _ in range(W)]

def set_px(c, x, y, col):
    if 0 <= x < W and 0 <= y < W:
        c[y][x] = col

def draw_vline(c, x, y0, y1, col):
    for y in range(y0, y1 + 1):
        set_px(c, x, y, col)

def draw_hline(c, y, x0, x1, col):
    for x in range(x0, x1 + 1):
        set_px(c, x, y, col)

def fill_rect(c, x0, y0, x1, y1, col):
    for y in range(y0, y1 + 1):
        for x in range(x0, x1 + 1):
            set_px(c, x, y, col)

def draw_circle(c, cx, cy, r, col, fill=True):
    for y in range(W):
        for x in range(W):
            d = (x - cx) ** 2 + (y - cy) ** 2
            if fill:
                if d <= r * r:
                    set_px(c, x, y, col)
            else:
                if (r - 0.7) ** 2 <= d <= (r + 0.7) ** 2:
                    set_px(c, x, y, col)

# ---------------- 作物各生长阶段贴图 ----------------
def draw_crop_stage(c, stage, max_age, stem_color, top_color):
    base_y = 15
    h = int(round(2 + (stage / max_age) * 12))
    top_y = base_y - h
    cx = 7
    draw_vline(c, cx, max(top_y, 0), base_y, stem_color)
    draw_vline(c, cx + 1, max(top_y, 0), base_y, stem_color)
    for ly in range(max(top_y + 2, 0), base_y, 3):
        draw_hline(c, ly, max(cx - 2, 0), min(cx + 3, 15), stem_color)
    if stage >= max_age - 1:
        draw_circle(c, cx, max(top_y, 1), 3, top_color, fill=True)
    elif stage >= 1:
        draw_circle(c, cx, max(top_y, 1), 1, top_color, fill=True)

# ---------------- 物品图标 ----------------
def draw_parsnip_item(c):
    tan = (235, 215, 170, 255); green = (90, 170, 70, 255)
    draw_circle(c, 8, 10, 4, tan, fill=True)
    fill_rect(c, 7, 13, 9, 14, tan)
    draw_vline(c, 7, 2, 8, green); draw_vline(c, 9, 2, 8, green); draw_vline(c, 8, 1, 8, green)

def draw_rhubarb_item(c):
    red = (200, 60, 60, 255); green = (90, 170, 70, 255)
    draw_vline(c, 8, 5, 14, red); draw_vline(c, 7, 6, 14, red); draw_vline(c, 9, 6, 14, red)
    draw_circle(c, 8, 3, 3, green, fill=True); draw_hline(c, 3, 5, 11, green)

def draw_rhubarb_seeds(c):
    bg = (210, 190, 150, 255); seed = (120, 80, 50, 255)
    fill_rect(c, 0, 0, 15, 15, bg)
    for (x, y) in [(4, 4), (11, 4), (4, 11), (11, 11), (8, 8)]:
        draw_circle(c, x, y, 1, seed, fill=True)

def draw_melon_slice(c):
    green = (80, 160, 60, 255); flesh = (235, 90, 90, 255); seed = (40, 30, 30, 255)
    draw_circle(c, 8, 8, 7, green, fill=True)
    draw_circle(c, 8, 8, 6, flesh, fill=True)
    for (x, y) in [(6, 6), (10, 6), (6, 10), (10, 10), (8, 8)]:
        set_px(c, x, y, seed)

def draw_melon_seeds(c):
    bg = (210, 190, 150, 255); seed = (50, 40, 40, 255)
    fill_rect(c, 0, 0, 15, 15, bg)
    for (x, y) in [(4, 4), (11, 4), (4, 11), (11, 11), (8, 8)]:
        draw_circle(c, x, y, 1, seed, fill=True)

def draw_amaranth_item(c):
    tan = (220, 200, 140, 255); red = (170, 60, 110, 255)
    draw_vline(c, 8, 6, 14, tan)
    draw_circle(c, 8, 4, 3, red, fill=True)
    for (x, y) in [(6, 4), (10, 4), (7, 6), (9, 6), (8, 3)]:
        set_px(c, x, y, tan)

def draw_amaranth_seeds(c):
    bg = (210, 195, 150, 255); seed = (150, 110, 60, 255)
    fill_rect(c, 0, 0, 15, 15, bg)
    for (x, y) in [(4, 4), (11, 4), (4, 11), (11, 11), (8, 8)]:
        draw_circle(c, x, y, 1, seed, fill=True)

def draw_frost_melon_item(c):
    blue = (170, 210, 235, 255); white = (235, 245, 255, 255)
    draw_circle(c, 8, 8, 7, blue, fill=True)
    set_px(c, 5, 5, white); set_px(c, 6, 5, white); set_px(c, 5, 6, white); set_px(c, 11, 11, white)

def draw_frost_melon_seeds(c):
    bg = (210, 190, 150, 255); seed = (70, 90, 120, 255)
    fill_rect(c, 0, 0, 15, 15, bg)
    for (x, y) in [(4, 4), (11, 4), (4, 11), (11, 11), (8, 8)]:
        draw_circle(c, x, y, 1, seed, fill=True)

def draw_melon_block(c):
    base = (110, 170, 80, 255); stripe = (180, 220, 150, 255)
    fill_rect(c, 0, 0, 15, 15, base)
    for x in range(0, 16, 4):
        draw_vline(c, x, 0, 15, stripe)

def draw_stem_tex(c):
    green = (80, 150, 60, 255)
    draw_vline(c, 7, 0, 15, green); draw_vline(c, 8, 0, 15, green)
    draw_hline(c, 10, 5, 11, green); draw_hline(c, 6, 4, 12, green)

def draw_attached_tex(c):
    green = (70, 140, 55, 255)
    draw_vline(c, 7, 0, 15, green); draw_vline(c, 8, 0, 15, green)

# ---------------- JSON 辅助 ----------------
def crop_blockstate(name, max_age):
    variants = {f"age={a}": {"model": f"{MODID}:block/crop/{name}/stage{a}"} for a in range(0, max_age + 1)}
    return {"variants": variants}

def crop_stage_model(name, a):
    return {"parent": "minecraft:block/crop",
            "textures": {"crop": f"{MODID}:block/crop/{name}/stage{a}"}}

def item_model(tex):
    return {"parent": "minecraft:item/generated",
            "textures": {"layer0": f"{MODID}:item/{tex}"}}

def bsp(age):
    return {"block": f"{MODID}:{age[0]}", "condition": "minecraft:block_state_property", "properties": {"age": age[1]}}

def crop_loot_self(name, item, max_age):
    return {
        "type": "minecraft:block",
        "functions": [{"function": "minecraft:explosion_decay"}],
        "pools": [{
            "bonus_rolls": 0.0,
            "entries": [{"type": "minecraft:alternatives", "children": [
                {"type": "minecraft:item", "conditions": [bsp((f"{name}_crop", str(max_age)))],
                 "functions": [{"function": "minecraft:set_count",
                                "count": {"type": "minecraft:uniform", "min": 1.0, "max": 4.0}}],
                 "name": f"{MODID}:{item}"},
                {"type": "minecraft:item", "name": f"{MODID}:{item}"}
            ]}],
            "rolls": 1.0
        }]
    }

def crop_loot_seed(name, item, seed, max_age, seed_max):
    return {
        "type": "minecraft:block",
        "functions": [{"function": "minecraft:explosion_decay"}],
        "pools": [
            {"bonus_rolls": 0.0,
             "entries": [{"type": "minecraft:alternatives", "children": [
                 {"type": "minecraft:item", "conditions": [bsp((f"{name}_crop", str(max_age)))],
                  "functions": [{"function": "minecraft:set_count",
                                 "count": {"type": "minecraft:uniform", "min": 1.0, "max": float(seed_max)}}],
                  "name": f"{MODID}:{seed}"},
                 {"type": "minecraft:item", "name": f"{MODID}:{seed}"}
             ]}],
             "rolls": 1.0},
            {"bonus_rolls": 0.0, "conditions": [bsp((f"{name}_crop", str(max_age)))],
             "entries": [{"type": "minecraft:item", "name": f"{MODID}:{item}"}], "rolls": 1.0}
        ]
    }

def melon_block_loot():
    return {
        "type": "minecraft:block",
        "pools": [{
            "bonus_rolls": 0.0,
            "entries": [{"type": "minecraft:item",
                         "functions": [{"function": "minecraft:set_count",
                                        "count": {"type": "minecraft:uniform", "min": 3.0, "max": 7.0}},
                                       {"function": "minecraft:explosion_decay"}],
                         "name": f"{MODID}:melon_slice"}],
            "rolls": 1.0
        }]
    }

# ---------------- 主流程 ----------------
def main():
    # 作物定义: (name, max_age, stem_color, top_color, item, seed_or_None, seed_max)
    crops = [
        ("parsnip", 3, (90, 170, 70, 255), (120, 200, 90, 255), "parsnip", None, 0),
        ("rhubarb", 4, (90, 170, 70, 255), (200, 60, 60, 255), "rhubarb", "rhubarb_seeds", 2),
        ("amaranth", 7, (120, 160, 80, 255), (180, 70, 120, 255), "amaranth", "amaranth_seeds", 3),
        ("frost_melon", 4, (90, 170, 70, 255), (170, 210, 235, 255), "frost_melon", "frost_melon_seeds", 1),
    ]

    tex_block = os.path.join(ASSETS, "textures", "block", "crop")
    tex_item = os.path.join(ASSETS, "textures", "item")
    mdl_block = os.path.join(ASSETS, "models", "block", "crop")
    mdl_item = os.path.join(ASSETS, "models", "item")
    bs = os.path.join(ASSETS, "blockstates")
    loot = os.path.join(DATA, "loot_table", "blocks")

    # 方块阶段贴图 + 模型 + 方块状态 + 战利品表
    for (name, max_age, sc, tc, item, seed, seed_max) in crops:
        d = os.path.join(tex_block, name); os.makedirs(d, exist_ok=True)
        for a in range(0, max_age + 1):
            c = new_canvas(); draw_crop_stage(c, a, max_age, sc, tc)
            write_png(os.path.join(d, f"stage{a}.png"), c)
            md = os.path.join(mdl_block, name); os.makedirs(md, exist_ok=True)
            with open(os.path.join(md, f"stage{a}.json"), "w", encoding="utf-8") as f:
                json.dump(crop_stage_model(name, a), f, indent=2, ensure_ascii=False)
        with open(os.path.join(bs, f"{name}_crop.json"), "w", encoding="utf-8") as f:
            json.dump(crop_blockstate(name, max_age), f, indent=2, ensure_ascii=False)
        with open(os.path.join(loot, f"{name}_crop.json"), "w", encoding="utf-8") as f:
            json.dump(crop_loot_self(name, item, max_age) if seed is None else crop_loot_seed(name, item, seed, max_age, seed_max),
                      f, indent=2, ensure_ascii=False)

    # 甜瓜：茎/附着茎/瓜体
    melon_d = os.path.join(tex_block, "melon"); os.makedirs(melon_d, exist_ok=True)
    c = new_canvas(); draw_stem_tex(c); write_png(os.path.join(melon_d, "stem.png"), c)
    c = new_canvas(); draw_attached_tex(c); write_png(os.path.join(melon_d, "attached_stem.png"), c)
    c = new_canvas(); draw_melon_block(c); write_png(os.path.join(melon_d, "melon.png"), c)

    mdir = os.path.join(mdl_block, "melon"); os.makedirs(mdir, exist_ok=True)
    with open(os.path.join(mdir, "stem.json"), "w", encoding="utf-8") as f:
        json.dump({"parent": "minecraft:block/cross",
                   "textures": {"cross": f"{MODID}:block/crop/melon/stem"}}, f, indent=2, ensure_ascii=False)
    with open(os.path.join(mdir, "attached_stem.json"), "w", encoding="utf-8") as f:
        json.dump({"parent": "minecraft:block/cross",
                   "textures": {"cross": f"{MODID}:block/crop/melon/attached_stem"}}, f, indent=2, ensure_ascii=False)
    with open(os.path.join(mdir, "melon_block.json"), "w", encoding="utf-8") as f:
        json.dump({"parent": "minecraft:block/cube_all",
                   "textures": {"all": f"{MODID}:block/crop/melon/melon"}}, f, indent=2, ensure_ascii=False)

    with open(os.path.join(bs, "melon_stem.json"), "w", encoding="utf-8") as f:
        json.dump({"variants": {f"age={a}": {"model": f"{MODID}:block/crop/melon/stem"} for a in range(0, 8)}},
                  f, indent=2, ensure_ascii=False)
    with open(os.path.join(bs, "attached_melon_stem.json"), "w", encoding="utf-8") as f:
        json.dump({"variants": {f"facing={d}": {"model": f"{MODID}:block/crop/melon/attached_stem"}
                                for d in ("north", "east", "south", "west")}},
                  f, indent=2, ensure_ascii=False)
    with open(os.path.join(bs, "melon.json"), "w", encoding="utf-8") as f:
        json.dump({"variants": {"": {"model": f"{MODID}:block/crop/melon/melon_block"}}}, f, indent=2, ensure_ascii=False)
    with open(os.path.join(loot, "melon.json"), "w", encoding="utf-8") as f:
        json.dump(melon_block_loot(), f, indent=2, ensure_ascii=False)

    # 物品图标 + 物品模型
    item_defs = [
        ("parsnip", draw_parsnip_item), ("rhubarb", draw_rhubarb_item),
        ("rhubarb_seeds", draw_rhubarb_seeds), ("melon_slice", draw_melon_slice),
        ("melon_seeds", draw_melon_seeds), ("amaranth", draw_amaranth_item),
        ("amaranth_seeds", draw_amaranth_seeds), ("frost_melon", draw_frost_melon_item),
        ("frost_melon_seeds", draw_frost_melon_seeds),
    ]
    for (name, fn) in item_defs:
        c = new_canvas(); fn(c); write_png(os.path.join(tex_item, f"{name}.png"), c)
        with open(os.path.join(mdl_item, f"{name}.json"), "w", encoding="utf-8") as f:
            json.dump(item_model(name), f, indent=2, ensure_ascii=False)

    # 语言文件
    zh = {
        "itemGroup.kaleidoscope_starduwally": "森罗物语：星灵之梦",
        "item.kaleidoscope_starduwally.garlic": "蒜",
        "item.kaleidoscope_starduwally.garlic_clove": "蒜瓣",
        "item.kaleidoscope_starduwally.cauliflower": "花椰菜",
        "item.kaleidoscope_starduwally.cauliflower_seeds": "花椰菜种子",
        "item.kaleidoscope_starduwally.kale": "甘蓝",
        "item.kaleidoscope_starduwally.kale_seeds": "甘蓝种子",
        "item.kaleidoscope_starduwally.parsnip": "防风草",
        "item.kaleidoscope_starduwally.rhubarb": "大黄",
        "item.kaleidoscope_starduwally.rhubarb_seeds": "大黄种子",
        "item.kaleidoscope_starduwally.melon_slice": "甜瓜片",
        "item.kaleidoscope_starduwally.melon_seeds": "甜瓜种子",
        "item.kaleidoscope_starduwally.amaranth": "苋菜",
        "item.kaleidoscope_starduwally.amaranth_seeds": "苋菜种子",
        "item.kaleidoscope_starduwally.frost_melon": "霜瓜",
        "item.kaleidoscope_starduwally.frost_melon_seeds": "霜瓜种子",
        "block.kaleidoscope_starduwally.melon": "甜瓜",
    }
    en = {
        "itemGroup.kaleidoscope_starduwally": "Kaleidoscope Starduwally",
        "item.kaleidoscope_starduwally.garlic": "Garlic",
        "item.kaleidoscope_starduwally.garlic_clove": "Garlic Clove",
        "item.kaleidoscope_starduwally.cauliflower": "Cauliflower",
        "item.kaleidoscope_starduwally.cauliflower_seeds": "Cauliflower Seeds",
        "item.kaleidoscope_starduwally.kale": "Kale",
        "item.kaleidoscope_starduwally.kale_seeds": "Kale Seeds",
        "item.kaleidoscope_starduwally.parsnip": "Parsnip",
        "item.kaleidoscope_starduwally.rhubarb": "Rhubarb",
        "item.kaleidoscope_starduwally.rhubarb_seeds": "Rhubarb Seeds",
        "item.kaleidoscope_starduwally.melon_slice": "Melon Slice",
        "item.kaleidoscope_starduwally.melon_seeds": "Melon Seeds",
        "item.kaleidoscope_starduwally.amaranth": "Amaranth",
        "item.kaleidoscope_starduwally.amaranth_seeds": "Amaranth Seeds",
        "item.kaleidoscope_starduwally.frost_melon": "Frost Melon",
        "item.kaleidoscope_starduwally.frost_melon_seeds": "Frost Melon Seeds",
        "block.kaleidoscope_starduwally.melon": "Melon",
    }
    with open(os.path.join(ASSETS, "lang", "zh_cn.json"), "w", encoding="utf-8") as f:
        json.dump(zh, f, indent=2, ensure_ascii=False)
    with open(os.path.join(ASSETS, "lang", "en_us.json"), "w", encoding="utf-8") as f:
        json.dump(en, f, indent=2, ensure_ascii=False)

    print("资源生成完成。")

if __name__ == "__main__":
    main()
