<template>
  <div>
    <div style="margin-top: 15px; margin-inline:5%; inline">
      <div style="float: right; display: flex">
        <el-input
          style="margin-left: 30px; width: 50%"
          v-model="inputUserName"
          placeholder="请输入用户名"
        ></el-input>
        <el-button
          slot="append"
          icon="el-icon-search"
          @click="handleSearchByName()"
        ></el-button>
        <el-button
          icon="el-icon-refresh"
          circle
          style="margin-right: 10px"
          @click="handleRefresh()"
        ></el-button>
      </div>
    </div>
    <div>
      <el-table :data="tableData" style="width: 100%">
        <el-table-column type="expand">
          <template slot-scope="props">
            <el-form
              label-position="left"
              inline
              class="demo-table-expand"
              style="margin-left: 268px"
            >
              <el-form-item label="用户名">
                <span>{{ props.row.userName }}</span>
              </el-form-item>
              <el-form-item label="性别">
                {{ props.row.gender | userGenderFilter }}
              </el-form-item>
              <el-form-item label="生日">
                <span>{{
                  $moment(props.row.birthday).format("YYYY-MM-DD")
                }}</span>
              </el-form-item>

              <el-form-item label="联系电话">
                <span>{{ props.row.mobile }}</span>
              </el-form-item>
            </el-form>
          </template>
          <el-pagination
            background
            style="margin-inline: 30%; margin-block: 2%"
            layout="prev, pager, next"
            :hide-on-single-page="true"
            :page-size="page.size"
            :current-page.sync="page.current"
            @current-change="handleCurrentChange"
            :total="page.total"
          >
          </el-pagination>
        </el-table-column>
        <el-table-column
          label="用户ID"
          prop="id"
          align="center"
        ></el-table-column>

        <el-table-column label="用户头像" prop="profilePhoto" align="center">
          <template slot-scope="scope" v-if="scope.row">
            <img
              style="width: 60px; height: 60px; border-radius: 50%"
              :src="
                scope.row.profilePhoto != null && scope.row.profilePhoto != ''
                  ? imageRootPath + scope.row.profilePhoto
                  : defaultAvatar
              "
            />
          </template>
        </el-table-column>
        <el-table-column
          label="用户名"
          prop="userName"
          align="center"
          sortable
        ></el-table-column>
        <el-table-column label="状态" prop="frozen" align="center" sortable>
          <template slot-scope="scope">
            <el-tag size="small">{{
              scope.row.frozen | userStatusFilter
            }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="注册时间" prop="createTime" sortable
          ><template slot-scope="scope"
            ><span>{{
              $moment(scope.row.createTime).format("YYYY-MM-DD HH:mm")
            }}</span></template
          ></el-table-column
        >
        <el-table-column prop="do" label="操作" width="200" align="center">
          <template slot-scope="scopeHandle">
            <el-button
              size="mini"
              type="danger"
              @click="showFreezeDialog(scopeHandle.$index, scopeHandle.row)"
              >{{ scopeHandle.row.frozen | writeButtonText }}</el-button
            >
          </template>
        </el-table-column>
      </el-table>

      <el-dialog
        title="提示"
        :visible.sync="FreezeDialogVisible"
        width="20%"
        center
      >
        <span style="margin-left: 30%"
          >确定要{{ this.changeStatusInfo }}吗</span
        >
        <span slot="footer" class="dialog-footer">
          <el-button @click="FreezeDialogVisible = false">取 消</el-button>
          <el-button
            type="primary"
            @click="(FreezeDialogVisible = false), handleFreeze()"
            >确 定</el-button
          >
        </span>
      </el-dialog>
      <el-pagination
        background
        style="margin-inline: 30%; margin-block: 2%"
        layout="prev, pager, next"
        :hide-on-single-page="true"
        :page-size="page.size"
        :current-page.sync="page.current"
        @current-change="handleCurrentChange"
        :total="page.total"
      >
      </el-pagination>
    </div>
  </div>
</template>
<style>
.demo-table-expand {
  font-size: 0;
}
.demo-table-expand label {
  width: 90px;
  color: #99a9bf;
}
.demo-table-expand .el-form-item {
  margin-right: 0;
  margin-bottom: 0;
  width: 42%;
}
</style>
<script>
import { mapGetters } from "vuex";
import defaultAvatar from "@/assets/image/defaultAvatar.png";
import { getUserList, freezeUser, getUserInfoByName } from "@/api/user";
const statusMap = {
  0: "可登录",
  1: "冻结中",
};
const genderMap = {
  0: "未选择",
  1: "男",
  2: "女",
};

export default {
  name: "user",
  filters: {
    userStatusFilter(status) {
      return statusMap[status];
    },
    userGenderFilter(gender) {
      return genderMap[gender];
    },
    writeButtonText(status) {
      return status == 0 ? "冻结" : "解冻";
    },
  },

  data() {
    return {
      page: {
        total: 0, //总条目数,页面加载时从后台获取
        current: 1, //当前页数
        size: 8, //页面显示条目大小
      },
      inputUserName: "", //输入待查找的用户ID
      tableData: [], //用户信息列表
      defaultAvatar: defaultAvatar, ///默认用户头像
      FreezeDialogVisible: false, //冻结/解冻用户对话框显示标志
      changeStatusInfo: "", //记录状态变化时提示信息
      userStatusInfo: {}, //用于用户状态变化
    };
  },
  computed: {
    ...mapGetters([
      "imageRootPath", //访问静态资源图片库根地址
    ]),
  },
  mounted() {
    this.setDefaultPage();
    this.WriteUserList();
  },
  methods: {
    //设置页面为默认值
    setDefaultPage() {
      this.page = {
        total: 0, //总条目数,页面加载时从后台获取
        current: 1, //当前页数
        size: 6, //页面显示条目大小
      };
    },
    WriteUserList() {
      //-1表示不区分用户状态信息
      getUserList(this.page, -1).then((res) => {
        if (res.success) {
          this.tableData = res.data.records; // 获取数据
          this.page.total = res.data.total;
        }
      });
    },
    //处理当前页码改变时请求新一页数据
    handleCurrentChange(val) {
      this.tableData = []; //解决table的index延迟的问题
      this.page.current = val; //获取当前点击的页码
      console.log("请求第几页", this.page.current);
      if (this.inputUserName === "") {
        //总列表换页
        this.WriteUserList();
      } else {
        //根据根据用户名换页获取数据
        this.getUserListByName();
      }
    },

    //请求根据用户名检索用户信息列表
    getUserListByName() {
      getUserInfoByName(this.page, this.inputUserName).then((res) => {
        console.log(res);
        if (res.success) {
          this.tableData = res.data.records; // 获取数据
          this.page.total = res.data.total;
        } else {
          this.tableData = [];
        }
      });
    },
    //根据用户名查找用户（mobile唯一）
    handleSearchByName() {
      if (this.inputUserName == "") {
        this.$message.warning("还没有输入内容哦！");
      } else {
        this.setDefaultPage();
        this.getUserListByName();
      }
    },
    //刷新数据列表
    handleRefresh() {
      this.inputUserName = "";
      this.setDefaultPage();
      this.WriteUserList();
      this.$notify({
        title: "刷新成功",
        type: "success",
        position: "bottom-right",
      });
    },

    //点击冻结/解冻按钮
    showFreezeDialog(index, row) {
      console.log(index, row);
      this.FreezeDialogVisible = true;
      this.changeStatusInfo = row.frozen == 0 ? "冻结" : "解冻";
      this.userStatusInfo = {
        id: row.id,
        frozen: row.frozen == 0 ? 1 : 0,
        index: index,
      };
    },
    //请求冻结/解冻
    handleFreeze() {
      console.log(this.userStatusInfo);

      freezeUser(this.userStatusInfo["id"], this.userStatusInfo["frozen"]).then(
        (res) => {
          if (res.success) {
            this.tableData[this.userStatusInfo["index"]].frozen =
              this.userStatusInfo["frozen"];
            this.$message({
              showClose: true,
              message: "操作成功",
              type: "success",
            });
          }
        }
      );
    },
  },
};
</script>
