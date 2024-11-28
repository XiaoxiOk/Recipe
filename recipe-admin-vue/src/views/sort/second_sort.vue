<template>
  <div style="padding-top: 10px; padding-left: 30px">
    <div>
      <div class="right-items" style="float: right; padding-right: 50px">
        <el-button
          type="primary"
          icon="el-icon-edit"
          @click="AddDialogVisible = true"
          >新增二级类别</el-button
        >
      </div>
    </div>
    <el-table
      :data="tableData"
      style="margin-top: 10px; margin-left: 30px"
      height="450"
      width="80%"
    >
      <el-table-column
        label="类别ID"
        prop="secId"
        width="100"
        align="center"
        sortable="true"
        sort-by="secId"
      ></el-table-column>

      <el-table-column
        prop="secName"
        label="类别名称"
        width="120"
        align="center"
      >
      </el-table-column>
      <el-table-column
        label="类别封面"
        width="100px"
        prop="image"
        align="center"
      >
        <template slot-scope="scope" v-if="scope.row">
          <img
            style="width: 80px; height: 60px"
            :src="
              scope.row.image != null && scope.row.image != ''
                ? imageRootPath + scope.row.image
                : defaultSort
            "
          />
        </template>
      </el-table-column>
      <el-table-column
        prop="firstName"
        label="上级类别"
        width="120"
        align="center"
      >
      </el-table-column>
      <el-table-column
        prop="createTime"
        label="创建时间"
        width="180"
        align="center"
        sortable
      >
        <template slot-scope="scope"
          ><span>{{
            $moment(scope.row.createTime).format("YYYY-MM-DD HH:mm")
          }}</span></template
        >
      </el-table-column>
      <el-table-column
        prop="updateTime"
        label="修改时间"
        width="180"
        align="center"
        sortable
      >
        <template slot-scope="scope"
          ><span>{{
            $moment(scope.row.updateTime).format("YYYY-MM-DD HH:mm")
          }}</span></template
        >
      </el-table-column>
      <el-table-column prop="do" label="操作" width="200" align="center">
        <template slot-scope="scope">
          <el-button size="mini" @click="handleEdit(scope.$index, scope.row)"
            >编辑</el-button
          >
          <el-button
            size="mini"
            @click="(DelDialogVisible = true), (indexDelete = scope.$index)"
            >移除</el-button
          >
        </template>
      </el-table-column>
    </el-table>
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
    <el-dialog title="提示" :visible.sync="DelDialogVisible" width="20%" center>
      <span style="margin-left: 30%">确定要删除吗</span>
      <span slot="footer" class="dialog-footer">
        <el-button @click="DelDialogVisible = false">取 消</el-button>
        <el-button
          type="primary"
          @click="(DelDialogVisible = false), handleDelete()"
          >确 定</el-button
        >
      </span>
    </el-dialog>
    <el-dialog
      title="新 增"
      width="30%"
      center
      :visible.sync="AddDialogVisible"
      :destroy-on-close="true"
    >
      <SecondEditSort
        ref="addSecondSort"
        elemType="addSecondSort"
        :formParentData="{}"
      ></SecondEditSort>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitAddForm()">确 定</el-button>
        <el-button @click="resetAddForm()">重 置</el-button>
      </span>
    </el-dialog>
    <el-dialog
      title="编 辑"
      width="30%"
      center
      v-if="EditDialogVisible"
      :visible.sync="EditDialogVisible"
      :destroy-on-close="true"
    >
      <SecondEditSort
        ref="editSecondSort"
        elemType="editSecondSort"
        :formParentData="this.formData"
        :firstSortId="firstSortId"
      ></SecondEditSort>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitEditForm()">确 定</el-button>
        <el-button @click="resetEditForm()">重 置</el-button>
      </span>
    </el-dialog>
  </div>
</template>
<script>
import { mapGetters } from "vuex";
import defaultSort from "@/assets/image/defaultSort.png";
import {
  getWholeSortByPage,
  addSecondSort,
  updateSecondSort,
  deleteSecondSort,
} from "@/api/sort";
import SecondEditSort from "./second_edit.vue";
export default {
  name: "sortList",
  components: { SecondEditSort },
  data() {
    return {
      page: {
        total: 0, //总条目数,页面加载时从后台获取
        current: 1, //当前页数
        size: 6, //页面显示条目大小
      },
      DelDialogVisible: false,
      AddDialogVisible: false,
      EditDialogVisible: false,
      indexDelete: null,
      tableData: [],
      formData: {}, //对应一个类别对象
      firstSortId: false, //已选一级类别
      defaultSort: defaultSort, ///默认类别封面
    };
  },
  computed: {
    ...mapGetters([
      "imageRootPath", //访问静态资源图片库根地址
    ]),
  },
  mounted() {
    this.page.current = 1;
    this.WriteSecSortList();
  },
  methods: {
    //加载类别列表
    WriteSecSortList() {
      getWholeSortByPage(this.page).then((res) => {
        console.log(res);
        this.tableData = res.data.records;
        this.page.total = res.data.total;
      });
    },
    //处理当前页码改变时请求新一页数据
    handleCurrentChange(val) {
      this.tableData = []; //解决table的index延迟的问题
      this.page.current = val; //获取当前点击的页码
      console.log("请求第几页", this.page.current);
      //总列表换页
      this.WriteSecSortList();
    },
    //点击编辑显示修改对话框
    handleEdit(index, row) {
      console.log(index, row);
      this.EditDialogVisible = true;
      this.formData = JSON.parse(JSON.stringify(row)); //深拷贝
      this.firstSortId = row.firstId;
      console.log(this.firstSortId);
    },
    //删除一条类别记录
    handleDelete() {
      console.log(this.tableData[this.indexDelete]);
      deleteSecondSort(this.tableData[this.indexDelete].secId).then((res) => {
        console.log(res);
        if (res.success) {
          this.$message({
            type: "success",
            message: "删除成功!",
          });
          this.tableData.splice(this.indexDelete, 1);
        }
      });
    },
    //重置新增类别表单
    resetAddForm() {
      this.$refs.addSecondSort.resetEditForm();
    },
    //重置编辑类别表单
    resetEditForm() {
      this.$refs.editSecondSort.resetEditForm();
    },
    //验证通过后申请添加类别
    submitAddForm() {
      this.$refs.addSecondSort.$refs.ruleForm.validate((validate) => {
        if (!validate) {
          return this.$message.error("请正确填写表单！");
        } else {
          console.log("通过验证");
          var sortInfo = this.$refs.addSecondSort.getSecondSortInfo();
          console.log("sortInfo>>>", sortInfo);
          addSecondSort(sortInfo).then((res) => {
            console.log(res);
            if (res.success) {
              this.AddDialogVisible = false;
              this.$message.success("添加成功！");
              this.formData = {};
              this.WriteSecSortList();
            }
          });
        }
      });
    },
    judgeSortInfoIsEdit(sortInfo) {
      console.log("judge", this.formData, "VS:", sortInfo);
      if (this.formData.secId != sortInfo.id) {
        return true;
      } else if (this.formData.image != sortInfo.image) {
        return true;
      } else if (this.formData.firstId != sortInfo.parent) {
        return true;
      } else if (this.formData.secName != sortInfo.typeName) {
        return true;
      } else {
        return false;
      }
    },
    //验证通过后申请修改分类信息
    submitEditForm() {
      this.$refs.editSecondSort.$refs.ruleForm.validate((validate) => {
        if (!validate) {
          return this.$message.error("请正确填写表单！");
        } else {
          var sortInfo = this.$refs.editSecondSort.getSecondSortInfo();
          console.log("formData:", this.formData);
          console.log("sortInfo:", sortInfo);
          if (this.judgeSortInfoIsEdit(sortInfo)) {
            updateSecondSort(sortInfo).then((res) => {
              if (res.success) {
                this.EditDialogVisible = false;
                this.$message.success("修改成功！");
                this.WriteSecSortList();
                this.formData = {};
              }
            });
          } else {
            this.$message({
              type: "warning",
              message: "拒绝操作：未作出任何修改！",
            });
          }
        }
      });
    },
  },
};
</script>