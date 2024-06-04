import { createRouter, createWebHashHistory } from "vue-router";

const router = createRouter({
  history: createWebHashHistory(import.meta.env.BASE_URL),
  routes: [
    // {
    //   path: "/home",
    //   name: "home",
    //   component: () => import("@/views/Home.vue"),
    // },
    {
      path:"/",
      name :"test",
      component:()=>import("@/views/test.vue")
    },
    {
      path:"/kb",
      name:"knowledgeBase",
      component:()=>import("@/views/KnowledgeBase.vue")
    }
  ],
});

export default router;
