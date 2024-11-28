import Vue from 'vue'
import Router from 'vue-router'

/* Layout */
import Layout from '@/views/layout/Layout'

Vue.use(Router)

/** note: Submenu only appear when children.length>=1
 *  detail see  https://panjiachen.github.io/vue-element-admin-site/guide/essentials/router-and-nav.html
 **/

/**
* hidden: true                   if `hidden:true` will not show in the sidebar(default is false)
* alwaysShow: true               if set true, will always show the root menu, whatever its child routes length
*                                if not set alwaysShow, only more than one route under the children
*                                it will becomes nested mode, otherwise not show the root menu
* redirect: noredirect           if `redirect:noredirect` will no redirect in the breadcrumb
* name:'router-name'             the name is used by <keep-alive> (must set!!!)
* meta : {
    roles: ['GET /aaa','POST /bbb']     will control the page roles (you can set multiple roles)
    title: 'title'               the name show in submenu and breadcrumb (recommend set)
    icon: 'svg-name'             the icon show in the sidebar,
    noCache: true                if true ,the page will no be cached(default is false)
  }
**/


export const constantRoutes = [
  {
    path: '/',
    component: Layout,
    redirect: 'login',
    children: [
      {
        path: 'dashboard',
        component: () => import('@/views/dashboard/index'),
        name: 'Dashboard',
        meta: { title: '首页', icon: 'el-icon-s-home', affix: true }
      }
    ]
  },
  {
    path: '/login',
    component: () => import('@/views/login/index'),
    hidden: true
  },
]

export const asyncRoutes = [
  {
    path: '/user',
    component: Layout,
    redirect: 'noredirect',
    name: 'userManage',
    meta: {
      title: '用户管理',
      icon: 'el-icon-s-custom'
    },
    children: [
      {
        path: 'user_list',
        component: () => import('@/views/user/user_list'),
        name: 'user_list',
        meta: {
          title: '用户管理',
          noCache: true
        }
      }
    ]
  },
  {
    path: '/recipe',
    component: Layout,
    redirect: 'noredirect',
    name: 'recipeManage',
    meta: {
      title: '菜谱管理',
      icon: 'el-icon-fork-spoon'
    },
    children: [
      {
        path: 'recipe_list',
        component: () => import('@/views/recipe/recipe_list'),
        name: 'list',
        meta: {
          title: '菜谱管理',
          noCache: true
        }
      },

    ]
  },
  {
    path: '/comment',
    component: Layout,
    redirect: 'noredirect',
    alwaysShow: true,
    name: 'commentManage',
    meta: {
      title: '评论管理',
      icon: 'el-icon-chat-dot-square'
    },
    children: [
      {
        path: 'comment_manage',
        component: () => import('@/views/comment/comment_list'),
        name: 'comment_manage',
        meta: {
          title: '顶层评论',
          noCache: true
        }
      },
      {
        path: 'reply_manage',
        component: () => import('@/views/comment/reply_list'),
        name: 'reply_manage',
        meta: {
          title: '评论回复',
          noCache: true
        }
      },
    ]
  },

  {
    path: '/sort',
    component: Layout,
    redirect: 'noredirect',
    alwaysShow: true,
    name: 'sortManage',
    meta: {
      title: '类别管理',
      icon: 'el-icon-menu'
    },
    children: [

      {
        path: 'first_sort',
        component: () => import('@/views/sort/first_sort'),
        name: 'sort',
        meta: {
          title: '一级类别',
          noCache: true
        }
      },
      {
        path: 'second_sort',
        component: () => import('@/views/sort/second_sort'),
        name: 'sort',
        meta: {
          title: '二级类别',
          noCache: true
        }
      }
    ]
  },

  {
    path: '/imageBoard',
    component: Layout,
    redirect: 'noredirect',
    // alwaysShow: true,
    name: 'ADManage',
    meta: {
      title: '广告管理',
      icon: 'el-icon-monitor'
      //  icon: 'el-icon-picture-outline'
    },
    children: [
      {
        path: 'ad',
        component: () => import('@/views/imageBoard/ad'),
        name: 'ad',
        meta: {
          title: '广告管理',
          noCache: true
        }
      },
    ]
  },
  {
    path: '/system',
    component: Layout,
    redirect: 'noredirect',
    alwaysShow: true,
    name: 'messageMange',
    meta: {
      title: '系统管理',
      icon: 'el-icon-s-tools'
    },
    children: [
      {
        path: 'admin_edit',
        component: () => import('@/views/system/admin_edit'),
        name: 'admin',
        meta: {
          title: '更改管理员信息',
          noCache: true
        }
      }
    ]
  },
]

const createRouter = () => new Router({
  mode: 'history', // require service support
  scrollBehavior: () => ({ y: 0 }),
  routes: constantRoutes
})

const router = createRouter()

// Detail see: https://github.com/vuejs/vue-router/issues/1234#issuecomment-357941465
export function resetRouter() {
  const newRouter = createRouter()
  router.matcher = newRouter.matcher // reset router
}

export default router
