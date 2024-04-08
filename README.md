<div align="center">
  <img width="300" src="https://github.com/Nova-Committee/CheatDetector/blob/master/logo.png?raw=true" alt="logo">

# CheatDetector
<p align="center">
    <a href="https://github.com/Nova-Committee/CheatDetector/issues">
      <img src="https://img.shields.io/github/issues/Nova-Committee/CheatDetector?style=flat" alt="issues" />
    </a>
    <a href="https://www.curseforge.com/minecraft/mc-mods/CheatDetector">
      <img src="http://cf.way2muchnoise.eu/cheatdetector.svg" alt="CurseForge Download">
    </a>
    <img src="https://img.shields.io/badge/license-GPLV3-green" alt="License">
    <a href="https://github.com/Nova-Committee/CheatDetector/actions/workflows/gradle.yml">
      <img src="https://github.com/Nova-Committee/CheatDetector/actions/workflows/gradle.yml/badge.svg" alt="Action">
    </a>  
</p>

一个纯客户端模组，检测一些常见的作弊行为。

### ***“误判是不可避免的，反作弊判定结果仅供参考。”***
</div>

## 检查
CheatDetector Mod使用了
[```TimeRecorder Mod```](https://github.com/Nova-Committee/TimeRecorder)
的部分时间性作弊检查，并额外添加了部分特色检查。。
- **Flight** 飞行检查
- **HighJump** 高跳检查
- **Blink** 闪现检查
- **NoSlow** 无减速检查
- **Speed** 速度检查
- **CreativeMode** 开创造检查

## 可调选项
CheatDetector Mod使用**Mod Menu**和**Cloth Config API**实现配置页面。
- 启用反作弊
- 禁用自我检查
- 最大移动偏移量
- 启用发包修复
- 发包修复模式
- 启用警报
- 启用发包修复警报
- 禁用警报缓冲区
- 警报缓冲区大小

## 兼容性
在服务器使用是安全的，**发包修复**可以帮助你避开一些误判。
对于玩法类模组的兼容性较弱，因此~~可能~~很可能出现大量误判。

## 如何使用
- 安装mod到客户端。 **（Fabric）**
- 启动游戏，通过**模组菜单**调整设置。

## 贡献
欢迎提issue或提交pull request。

## 相关项目
- [TimeRecorder](https://github.com/Nova-Committee/TimeRecorder)
- [Cloth Config](https://github.com/shedaniel/cloth-config)
