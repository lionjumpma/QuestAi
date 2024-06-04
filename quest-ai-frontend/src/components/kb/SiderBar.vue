<!--
@Author: 唐莺芸
@Date: 2024/4/28
@Description: 创建一个带有折叠功能的侧边栏菜单，用户可以通过单选按钮组来切换菜单的折叠状态。
当菜单展开或关闭时，会调用相应的事件处理方法，并在控制台打印相关信息。
-->
<template>
  <div class="sidebar-container">
    <el-radio-group v-model="isCollapse" class="radio-group">
      <el-radio-button :value="false">Expand</el-radio-button>
      <el-radio-button :value="true">Collapse</el-radio-button>
    </el-radio-group>
    <el-menu
        default-active="2"
        class="el-menu-vertical-demo"
        :collapse="isCollapse"
        @open="handleOpen"
        @close="handleClose"
    >
      <el-sub-menu index="1">
        <template #title>
          <el-icon><Location /></el-icon>
          <span>Navigator One</span>
        </template>
        <el-menu-item-group>
          <template #title><span>Group One</span></template>
          <el-menu-item index="1-1">item one</el-menu-item>
          <el-menu-item index="1-2">item two</el-menu-item>
        </el-menu-item-group>
        <el-menu-item-group title="Group Two">
          <el-menu-item index="1-3">item three</el-menu-item>
        </el-menu-item-group>
        <el-sub-menu index="1-4">
          <template #title><span>item four</span></template>
          <el-menu-item index="1-4-1">item one</el-menu-item>
        </el-sub-menu>
      </el-sub-menu>
      <el-menu-item index="2">
        <el-icon><IconMenu /></el-icon>
        <template #title>Navigator Two</template>
      </el-menu-item>
      <el-menu-item index="3" disabled>
        <el-icon><Document /></el-icon>
        <template #title>Navigator Three</template>
      </el-menu-item>
      <el-menu-item index="4">
        <el-icon><Setting /></el-icon>
        <template #title>Navigator Four</template>
      </el-menu-item>
    </el-menu>
  </div>
</template>

<script lang="ts" setup>
import { ref } from 'vue'
import {
  Document,
  Menu as IconMenu,
  Location,
  Setting,
} from '@element-plus/icons-vue'

const isCollapse = ref(true)
const handleOpen = (key: string, keyPath: string[]) => {
  console.log(key, keyPath)
}
const handleClose = (key: string, keyPath: string[]) => {
  console.log(key, keyPath)
}
</script>

<style>
.sidebar-container {
  display: flex;
  flex-direction: column;
  width: 200px;
}

.radio-group {
  margin-bottom: 20px;
}

.el-menu-vertical-demo:not(.el-menu--collapse) {
  width: 100%; /* Use 100% width to fit the container */
  min-height: 400px;
}
</style>
