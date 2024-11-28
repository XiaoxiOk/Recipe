<template>
  <div>
    <div style="margin-top: 15px; margin-inline:5%; inline">
      <div style="float: right; display: flex">
        <el-input-number
          :controls="false"
          style="margin-left: 30px; width: 50%"
          v-model.number="inputTypeId"
          placeholder="请输入类型编号"
        ></el-input-number>
        <el-select
          v-model="selectedType"
          placeholder="请选择状态"
          style="margin-left: 10px; margin-right: 10px; width: 150px"
        >
          <el-option
            v-for="item in indexOptions"
            :key="item.value"
            :value="item.value"
            :label="item.label"
          >
          </el-option>
        </el-select>
        <el-button
          slot="append"
          icon="el-icon-search"
          @click="handleSearch()"
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
        <el-table-column
          label="评论编号"
          fixed="left"
          prop="id"
          width="150px"
          align="center"
        ></el-table-column>

        <el-table-column
          label="用户昵称"
          prop="userName"
          align="center"
        ></el-table-column>
        <el-table-column
          label="用户头像"
          prop="profilePhoto"
          align="center"
          width="120px"
        >
          <template slot-scope="scope" v-if="scope.row">
            <img
              style="width: 40px; height: 40px; border-radius: 50%"
              :src="
                scope.row.profilePhoto != null && scope.row.profilePhoto != ''
                  ? imageRootPath + scope.row.profilePhoto
                  : defaultAvatar
              "
            />
          </template>
        </el-table-column>
        <el-table-column
          label="评论内容"
          prop="content"
          width="200px"
          :show-overflow-tooltip="true"
          align="left"
        ></el-table-column>
        <el-table-column
          label="评论类型"
          prop="topicType"
          width="100px"
          align="center"
        >
          <template slot-scope="scope">
            <el-tag size="small" effect="plain" type="warning">{{
              scope.row.topicType | commentTypeFilter
            }}</el-tag>
          </template>
        </el-table-column>

        <el-table-column
          label="类型编号"
          prop="topicId"
          width="120px"
          align="center"
        ></el-table-column>
        <el-table-column
          label="发表时间"
          prop="createTime"
          width="180"
          sortable
          align="center"
          ><template slot-scope="scope"
            ><span>{{
              $moment(scope.row.createTime).format("YYYY-MM-DD HH:mm")
            }}</span></template
          ></el-table-column
        >
        <el-table-column
          prop="do"
          label="操作"
          fixed="right"
          width="120px"
          align="center"
        >
          <template slot-scope="scopeHandle">
            <el-button
              size="mini"
              type="danger"
              @click="showDeleteDialog(scopeHandle.$index, scopeHandle.row)"
              >删除</el-button
            >
          </template>
        </el-table-column>
      </el-table>

      <el-dialog
        title="提示"
        :visible.sync="DeleteDialogVisible"
        width="20%"
        center
      >
        <span style="margin-left: 20%">确定要删除该评论吗</span>
        <span slot="footer" class="dialog-footer">
          <el-button @click="DeleteDialogVisible = false">取 消</el-button>
          <el-button
            type="primary"
            @click="(DeleteDialogVisible = false), handleDelete()"
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
import { getCommentsByPage, deleteComment } from "@/api/comment"; // getRecipeInfoByName,
const typeMap = {
  1: "菜谱",
  2: "动态",
};

export default {
  name: "comment",
  filters: {
    commentTypeFilter(type) {
      return typeMap[type];
    },
  },

  data() {
    return {
      indexOptions: [
        { value: 1, label: "菜谱" },
        { value: 2, label: "动态" },
      ],
      page: {},
      selectedType: "", //评论类型类型-1表示不区分类型
      inputTypeId: undefined, //输入待查找的菜谱ID
      tableData: [], //菜谱信息列表
      defaultAvatar: defaultAvatar, ///默认菜谱封面
      DeleteDialogVisible: false, //冻结/解冻菜谱对话框显示标志
    };
  },
  computed: {
    ...mapGetters([
      "imageRootPath", //访问静态资源图片库根地址
    ]),
  },
  mounted() {
    this.setDefaultPage();
    this.WriteCommentList();
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
    WriteCommentList() {
      getCommentsByPage(this.page, -1, -1).then((res) => {
        console.log(res);
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
      if (this.inputTypeId === undefined && this.selectedType === "") {
        //总列表换页
        this.WriteCommentList();
      } else {
        //根据所选择的状态类型换页获取数据
        this.getSearchList();
      }
    },
    //请求根据所输入的菜谱名或状态或一起来筛选列表
    getSearchList() {
      var type, typeId;
      if (this.selectedType === "") {
        type = -1;
      } else {
        type = this.selectedType;
      }
      if (this.inputTypeId === undefined) {
        typeId = -1;
      } else {
        typeId = this.inputTypeId;
      }
      console.log("type-typeId:", type, typeId);
      getCommentsByPage(this.page, type, typeId).then((res) => {
        console.log(res);
        if (res.success) {
          this.tableData = res.data.records; // 获取数据
          this.page.total = res.data.total;
        } else {
          this.tableData = [];
        }
      });
    },
    //根据菜谱名查找菜谱（mobile唯一）
    handleSearch() {
      if (this.inputTypeId == undefined && this.selectedType === "") {
        this.$message.warning("还没有输入/选择筛选内容哦！");
      } else {
        this.setDefaultPage();
        this.getSearchList();
      }
    },
    //刷新数据列表
    handleRefresh() {
      this.inputTypeId = undefined;
      this.selectedType = "";
      this.page.current = 1; //将页码置1
      this.WriteCommentList();
      this.$notify({
        title: "刷新成功",
        type: "success",
        position: "bottom-right",
      });
    },
    showContent(index, row) {},
    //点击冻结/解冻按钮
    showDeleteDialog(index, row) {
      console.log(index, row);
      this.DeleteDialogVisible = true;

      this.commentInfo = {
        id: row.id,
        topicId: row.topicId,
        topicType: row.topicType,
        index: index,
      };
    },
    //请求删除评论
    handleDelete() {
      console.log(this.commentInfo);
      console.log("go to delete!!");
      deleteComment(this.commentInfo).then((res) => {
        if (res.success) {
          this.inputTypeId = undefined;
          this.selectedType = "";
          this.page.current = 1; //将页码置1
          this.WriteCommentList();
          this.$message({
            showClose: true,
            message: "操作成功",
            type: "success",
          });
        }
      });
    },
  },
};
</script>
