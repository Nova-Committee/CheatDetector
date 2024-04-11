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

## 反作弊
反作弊 模块旨在不被发现的情况下揭示其他玩家的作弊行为。
- **Flight** 飞行检查
- **HighJump** 高跳检查
- **Blink** 闪现检查
- **NoSlow** 无减速检查
- **Speed** 速度检查
- **GameMode** 游戏模式检查
- **Velocity** 反击退检查

## 修复
修复 模块旨在避免被生电服务器反作弊错误标记，不要使用它们获取**不平等的优势**。
- 针对Themis反作弊
    - tweakeroo右键连点器修复
- 针对Vulcan反作弊
    - BadPacket (Type 1)
    - BadPacket (Type 2)
    - Movement Disabler (适用于Vulcan 2.7.5)
- Flag Detector (标记检测器)
  基于拉回帮助你确认自己是否被反作弊标记。

## 可调选项
CheatDetector Mod使用**Mod Menu**和**Cloth Config API**实现配置页面。

Mod包含69+项自定义选项，对于不同服务器，你可能需要根据延迟、丢包等情况修改配置。

默认设置为<80ms的服务器设计。

## 兼容性
在服务器使用是安全的，服务器无法通过你的发包了解到此Mod的安装情况。

对于玩法类模组的兼容性较弱。如果在模组服使用，~~可能~~很可能出现大量误判。

## 如何使用
- 根据你的游戏版本，选择Mod版本。
- 安装mod到客户端。 **（Fabric）**
- 启动游戏，通过**模组菜单**调整设置。

## 贡献
欢迎提issue或提交pull request。
如果你想和我共同开发这个项目，请联系我。

## 相关项目
- [TimeRecorder](https://github.com/Nova-Committee/TimeRecorder) （反作弊 模块在此mod基础上开发）
- [Cloth Config](https://github.com/shedaniel/cloth-config) （感谢这么好用的自动配置！）
