<template>
  <div style="padding-top: 15px; padding-left: 30px">
    <div class="right-items" style="float: right; display: flex">
      <el-input
        maxlength="20"
        ref="newInput"
        v-model="newType"
        placeholder="请输入新增类别"
      ></el-input>
      <el-button
        plain
        type="primary"
        style="margin-right: 50px"
        @click="handleAddNew"
        >添加</el-button
      >
    </div>

    <el-table
      :data="tableData"
      style="margin-top: 35px; margin-left: 40px"
      height="400"
      width="80%"
    >
      <el-table-column
        label="类别ID"
        prop="id"
        width="130"
        align="center"
      ></el-table-column>

      <el-table-column
        prop="typeName"
        label="类别名称"
        width="180"
        align="center"
      ></el-table-column>

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
      title="编 辑"
      width="30%"
      center
      v-if="EditDialogVisible"
      :visible.sync="EditDialogVisible"
      :destroy-on-close="true"
    >
      <el-input
        ref="editInput"
        maxlength="20"
        v-model="editTypeName"
      ></el-input>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitEditForm()">确 定</el-button>
        <el-button @click="resetEditField()">重 置</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import {
  getFirstSortList,
  updateFirstSort,
  addFirstSort,
  deleteFirstSort,
} from "@/api/sort";
export default {
  data() {
    return {
      page: {
        total: 0, //总条目数,页面加载时从后台获取
        current: 1, //当前页数
        size: 6, //页面显示条目大小
      },
      newType: "", //新增类别名称
      tableData: [], //类别信息列表
      editTypeName: "", //编辑后的类别名称
      beforeEditType: [], //编辑前类别数据
      EditDialogVisible: false, //编辑对话框显示标志
      DelDialogVisible: false, //删除对话框显示标志
    };
  },
  mounted() {
    this.page.current = 1;
    this.WriteFirstSortList();
  },
  methods: {
    WriteFirstSortList() {
      getFirstSortList(this.page).then((res) => {
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
      this.WriteFirstSortList();
    },
    //点击编辑显示修改对话框
    handleEdit(index, row) {
      this.EditDialogVisible = true;
      this.beforeEditType = JSON.parse(JSON.stringify(row)); //深拷贝
      this.editTypeName = row.typeName;
    },
    validateTextLength(value) {
      // 中文、中文标点、全角字符按1长度，英文、英文符号、数字按0.5长度计算
      let cnReg = /([\u4e00-\u9fa5]|[\u3000-\u303F]|[\uFF00-\uFF60])/g;
      let mat = value.match(cnReg);
      let length;
      if (mat) {
        length = mat.length + (value.length - mat.length) * 0.5;
        return length;
      } else {
        return value.length * 0.5;
      }
    },
    //判断类别名称长度
    judgeTypeLength(typeName) {
      var len = this.validateTextLength(typeName);
      if (len >= 2 && len <= 20) {
        return true;
      } else {
        return false;
      }
    },
    //重置表单
    resetEditField() {
      console.log("reset...");
      this.editTypeName = this.beforeEditType.typeName;
    },
    //验证通过后申请修改分类信息
    submitEditForm() {
      if (this.judgeTypeLength(this.editTypeName)) {
        var sortInfo = {
          id: this.beforeEditType.id, //一级类别ID
          typeName: this.editTypeName, //一级类别新名称
          createTime: this.beforeEditType.createTime, //一级级类别创建时间
          updateTime: this.beforeEditType.updateTime, //一级类别修改时间
        };
        updateFirstSort(sortInfo).then((res) => {
          if (res.success) {
            this.EditDialogVisible = false;
            this.$message.success("修改成功！");
            this.WriteFirstSortList();
            this.formData = {};
          } else {
            this.$message({
              type: "info",
              message: res.data.msg,
            });
            this.$refs.editInput.focus();
          }
        });
      } else {
        this.$message({
          type: "info",
          message: "长度应为2~20个字符!",
        });
        this.$refs.editInput.focus();
      }
    },
    //验证通过后申请添加类别
    handleAddNew() {
      if (this.judgeTypeLength(this.newType)) {
        var newSort = {
          typeName: this.newType, //一级类别新名称
        };
        addFirstSort(newSort).then((res) => {
          if (res.success) {
            this.newType = "";
            this.$message.success("添加成功！");
            this.WriteFirstSortList();
          } else {
            this.$message({
              type: "info",
              message: res.data.msg,
            });
            this.$refs.newInput.focus(); //光标定位在输入框
          }
        });
      } else {
        this.$message({
          type: "info",
          message: "长度应为2~20个字符!",
        });
        this.$refs.newInput.focus();
      }
    },
    //删除一条类别记录
    handleDelete() {
      deleteFirstSort(this.tableData[this.indexDelete].id).then((res) => {
        if (res.success) {
          this.$message({
            type: "success",
            message: "删除成功!",
          });
          this.tableData.splice(this.indexDelete, 1);
        }
      });
    },
  },
};
</script>