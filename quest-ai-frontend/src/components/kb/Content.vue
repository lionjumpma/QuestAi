<!--
@Author: 何孟燃
@Date: 2024/4/27
@Description: 创建一个显示应用列表的界面。它使用 Vue 的 v-for 指令来循环遍历一个数组，并为每个应用创建一个显示其标题、描述和按钮的容器。
此外，还有一个用于创建新应用的按钮。
-->
<template>
  <div class="flex-1 p-10">
    <h1 class="text-2xl font-semibold text-gray-700">我的应用</h1>
    <div class="grid grid-cols-4 gap-6 mt-6">
      <div v-for="app in apps" :key="app.id" class="bg-white p-6 rounded-lg shadow-lg">
        <div class="flex items-center space-x-6 mb-4">
          <div class="flex-shrink-0">
            <i class="fas fa-folder text-blue-500 text-4xl"></i>
          </div>
          <div>
            <h2 class="text-lg font-semibold text-gray-700">{{ app.title }}</h2>
            <p class="text-gray-600">{{ app.description }}</p>
            <button class="mt-4 text-blue-600 hover:underline">团队</button>
          </div>
        </div>
      </div>
    </div>
    <div class="flex justify-end mt-6">
      <button class="bg-blue-500 text-white px-4 py-2 rounded-md font-semibold hover:bg-blue-600 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-opacity-50">
        + 新建
      </button>
    </div>
  </div>
</template>

<script>
export default {
  name: 'Content',
  data() {
    return {
      apps: [
        { id: 1, title: '知识库', description: '这个应用还没写介绍~' },
        { id: 2, title: '知识库+知识引擎', description: '这个应用还没写介绍~' },
        { id: 3, title: '问题分析+知识库', description: '这个应用还没写介绍~' },
        { id: 4, title: '知识引擎+文档', description: '这个应用还没写介绍~' }
      ]
    };
  }
}
</script>

<style scoped>
/* Add any scoped styles if necessary */
</style>
