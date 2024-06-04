<!--
@Author: 何孟燃
@Date: 2024/5/1
@Description: 定义了组件的数据、方法、生命周期钩子以及与 Vuex 相关的逻辑,使用 marked 和 highlight.js 来渲染 Markdown 文本，并与后端 API 进行交互以获取及更新对话数据，是对test1的修改
-->
<template>
<!--  <div class="min-h-screen bg-gray-100">-->
    <div class="flex">
      <Sidebar />
<!--      <Content />-->
    </div>
<!--  </div>-->
</template>

<script setup>
import Sidebar from '@/components/kb/SiderBar.vue';
import Content from '@/components/kb/Content.vue';

</script>

<style>
/* Add any global styles if necessary */
</style>
