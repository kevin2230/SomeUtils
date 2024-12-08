# 字幕图片拼接工具

这是一个Android应用，用于处理视频截图中的字幕拼接。

## 主要功能

1. 从相册中选择多张带字幕的视频截图
2. 智能拼接：
   - 完整显示第一张图片
   - 后续图片仅显示字幕部分
3. 图片管理：
   - 动态添加新图片
   - 左滑显示删除按钮，支持删除单张图片
4. 实时预览：
   - 使用RecyclerView实时显示拼接效果
   - 最终导出时才进行实际图片合成

## 交互设计

- 支持左滑删除功能
- 删除时显示红色背景和删除图标
- 图片以卡片形式展示
- 实时预览字幕拼接效果

## 技术实现

- 使用Kotlin开发
- 图片选择使用ActivityResult API
- 使用RecyclerView实现预览
- 使用Bitmap处理图片拼接
- 使用ItemTouchHelper和自定义SwipeToDeleteCallback实现滑动删除
- 使用CardView实现图片卡片效果

## 代码结构

- base/: 基础组件
  - BaseToolFragment: Fragment基类
- feature/: 功能模块
  - imagestitching/: 图片拼接功能
    - adapter/: 适配器
    - helper/: 辅助类
    - util/: 工具类
 