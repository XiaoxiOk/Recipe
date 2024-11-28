<template>
  <div>
    <div style="margin-top: 15px; margin-inline:5%; inline">
      <div style="float: right; display: flex">
        <el-input
          style="margin-left: 30px; width: 50%"
          v-model="inputRecipeName"
          placeholder="请输入菜谱名"
        ></el-input>
        <el-select
          v-model="selectedState"
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
          label="菜谱ID"
          prop="id"
          align="center"
        ></el-table-column>

        <el-table-column label="菜谱封面" prop="showImage" align="center">
          <template slot-scope="scope" v-if="scope.row">
            <img
              style="width: 100px; height: 80px"
              :src="
                scope.row.showImage != null && scope.row.showImage != ''
                  ? imageRootPath + scope.row.showImage
                  : recipeImgDefault
              "
            />
          </template>
        </el-table-column>
        <el-table-column
          label="菜谱名"
          prop="recipeName"
          align="center"
          sortable
        ></el-table-column>
        <el-table-column label="状态" prop="state" align="center" sortable>
          <template slot-scope="scope">
            <el-tag size="small">{{
              scope.row.state | recipeStatusFilter
            }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="上传时间" prop="createTime" sortable
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
              type="warning"
              plain
              @click="
                showRecipeDetailDialog(scopeHandle.$index, scopeHandle.row)
              "
              >详细查看</el-button
            >
            <el-button
              size="mini"
              type="danger"
              @click="showFreezeDialog(scopeHandle.$index, scopeHandle.row)"
              >{{ scopeHandle.row.state | writeButtonText }}</el-button
            >
          </template>
        </el-table-column>
      </el-table>
      <el-dialog title="菜谱详情" :visible.sync="DetailInfoVisible">
        <div class="container">
          <div class="recipe-info">
            <el-image
              fit="cover"
              class="showImage"
              :src="
                itemDetail.showImage != null && itemDetail.showImage != ''
                  ? imageRootPath + itemDetail.showImage
                  : recipeImgDefault
              "
            ></el-image>
            <!-- <Video
              v-if="itemDetail.showImage != itemDetail.mediaUrl"
              :src="itemDetail.mediaUrl"
              :poster="itemDetail.showImage"
            /> -->
            <div class="recipe-name">{{ itemDetail.recipeName }}</div>
          </div>

          <div class="box_css">
            <!-- 详情说明 -->
            <div class="content">{{ itemDetail.detail }}</div>
            <!-- end 详情说明  -->
            <!-- 用料 -->
            <div class="materials-box">
              <div
                v-if="
                  itemDetail.materials != undefined &&
                  itemDetail.materials.length > 0
                "
              >
                <span class="title_yl">用料</span>
                <div v-for="(item, index) in itemDetail.materials" :key="index">
                  <div class="material-item">
                    <span>{{ item.foodName }}</span>
                    <span>{{ item.consumption }}</span>
                  </div>
                </div>
              </div>
            </div>
            <!-- end 用料 -->

            <!-- 步骤 -->
            <div class="steps-box">
              <div
                v-if="
                  itemDetail.steps != undefined && itemDetail.steps.length > 0
                "
              >
                <div v-for="(item, index) in itemDetail.steps" :key="index">
                  <span class="title_step">步骤{{ item.seqNumber }}</span>
                  <div class="step-item">
                    <el-image
                      fit="cover"
                      v-if="item.imgUrl != null && item.imgUrl != ''"
                      class="showImage showImageStep"
                      :src="
                        item.imgUrl != null && item.imgUrl != ''
                          ? imageRootPath + item.imgUrl
                          : ''
                      "
                    ></el-image>

                    <span>{{ item.description }}</span>
                  </div>
                </div>
              </div>
            </div>
            <!-- end 用料 -->
          </div>
        </div>
      </el-dialog>
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
            @click="(FreezeDialogVisible = false), handleOnOrOff()"
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


<script>
import { mapGetters } from "vuex";
import recipeImgDefault from "@/assets/image/recipe-default.jpg";
import {
  getRecipeBasicByPage,
  getRecipeAppendById,
  updateRecipeStateById,
  searchRecipeByNameOrState,
} from "@/api/recipe";
const statusMap = {
  0: "上架中",
  1: "已下架",
};
const rateMap = {
  0: "容易",
  1: "较易",
  2: "中等",
  3: "较难",
  4: "老手",
};

export default {
  name: "recipe",
  // components: {
  //   Video,
  // },
  filters: {
    recipeStatusFilter(status) {
      return statusMap[status];
    },
    recipeRateFilter(rate) {
      return rateMap[rate];
    },
    writeButtonText(status) {
      return status == 0 ? "下架" : "上架";
    },
  },

  data() {
    return {
      indexOptions: [
        { value: 0, label: "上架中" },
        { value: 1, label: "已下架" },
      ], //为类型选择器提供数据
      page: {},
      selectedState: "", //菜谱状态-1表示不区分状态
      inputRecipeName: "", //输入待查找的菜谱ID
      tableData: [], //菜谱信息列表
      recipeImgDefault: recipeImgDefault, ///默认菜谱封面
      itemDetail: {}, //菜谱基本+用料+步骤信息
      DetailInfoVisible: false,
      FreezeDialogVisible: false, //冻结/解冻菜谱对话框显示标志
      changeStatusInfo: "", //记录状态变化时提示信息
      recipeStatusInfo: {}, //用于菜谱状态变化
    };
  },
  computed: {
    ...mapGetters([
      "imageRootPath", //访问静态资源图片库根地址
    ]),
  },
  mounted() {
    this.setDefaultPage();
    this.WriteRecipeList();
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
    WriteRecipeList() {
      getRecipeBasicByPage(this.page, -1).then((res) => {
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
      if (this.selectedState === "" && this.inputRecipeName === "") {
        //总列表换页
        this.WriteRecipeList();
      } else {
        //根据所选择的状态类型换页获取数据
        this.getSearchList();
      }
    },
    //请求根据所输入的菜谱名或状态或一起来筛选列表
    getSearchList() {
      var state;
      if (this.selectedState === "") {
        state = -1;
      } else {
        state = this.selectedState;
      }
      console.log(state);
      searchRecipeByNameOrState(this.page, this.inputRecipeName, state).then(
        (res) => {
          console.log(res);
          if (res.success) {
            this.tableData = res.data.records; // 获取数据
            this.page.total = res.data.total;
          } else {
            this.tableData = [];
          }
        }
      );
    },
    //根据菜谱名查找菜谱（mobile唯一）
    handleSearch() {
      if (this.inputRecipeName === "" && this.selectedState === "") {
        this.$message.warning("还没有输入/选择筛选内容哦！");
      } else {
        this.setDefaultPage();
        this.getSearchList();
      }
    },
    //刷新数据列表
    handleRefresh() {
      this.inputRecipeName = "";
      this.selectedState = ""; //将类型置1
      this.page.current = 1; //将页码置1
      this.WriteRecipeList();
      this.$notify({
        title: "刷新成功",
        type: "success",
        position: "bottom-right",
      });
    },
    //点击详情查看按钮
    showRecipeDetailDialog(index, row) {
      if (this.itemDetail.id != undefined && this.itemDetail.id == row.id) {
        this.DetailInfoVisible = true;
        console.log("same!!");
        return;
      } else {
        this.itemDetail = {}; //注意清空
        getRecipeAppendById(row.id).then((res) => {
          console.log("getRecipeAppendById--res:", res);
          if (res.success) {
            this.itemDetail = {
              id: row.id,
              recipeName: row.recipeName,
              detail: row.detail,
              difficulty: row.difficulty,
              showImage: row.showImage,
              createTime: row.createTime,
              steps: res.data.steps,
              materials: res.data.materials,
            };
            console.log("itemDetail:", this.itemDetail);
            this.DetailInfoVisible = true;
          } else {
            this.$message({
              showClose: true,
              message: "后台服务器跑路了",
              type: "warning",
            });
          }
        });
      }
    },

    //点击冻结/解冻按钮
    showFreezeDialog(index, row) {
      console.log(index, row);
      this.FreezeDialogVisible = true;
      this.changeStatusInfo = row.state == 0 ? "下架" : "上架";
      this.recipeStatusInfo = {
        id: row.id,
        state: row.state == 0 ? 1 : 0,
        index: index,
      };
    },
    //请求上架/下架
    handleOnOrOff() {
      console.log(this.recipeStatusInfo);
      updateRecipeStateById(
        this.recipeStatusInfo["id"],
        this.recipeStatusInfo["state"]
      ).then((res) => {
        if (res.success) {
          this.tableData[this.recipeStatusInfo["index"]].state =
            this.recipeStatusInfo["state"];
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
.container {
  height: auto;
  box-sizing: border-box;
  padding: 0 0 10px;
  background-color: #fff;
}
.recipe-info {
  position: relative;
  background: #fff;
}
.showImage {
  width: 80%;
  height: 250px;
  display: block;
  margin: 0 auto;
  border-radius: 10px;
}
.showImageStep {
  width: 100%;
  height: 300px;
}
.step-item span {
  padding: 10px 0 0;
  font-size: 14px;
  line-height: 20px;
  display: block;
}
.recipe-name {
  margin: 10px 0;
  font-size: 20px;
  line-height: 20px;
  color: #333;
  box-sizing: border-box;
  font-weight: bold;
  text-align: center;
}
.box_css {
  background-color: #fff;
  box-sizing: border-box;
  padding: 0 8px;
}
.content {
  line-height: 18px;
  display: block;
  text-indent: 2em;
  box-sizing: border-box;
  padding: 0px 0 10px;
  border-bottom: 1px solid rgb(90, 86, 86);
}
.title_yl {
  font-size: 20px;
  font-weight: bold;
  line-height: 40px;
  margin-top: 10px;
  display: block;
}
.material-item {
  display: flex;
  justify-content: space-between;
  padding: 0 5px 10px;
  border-bottom: 1px dashed rgb(90, 86, 86);
  margin: 10px auto;
}
.steps-box {
  padding-bottom: 10px;
  margin-bottom: 20px;
}
.title_step {
  font-size: 16px;
  font-weight: bold;
  line-height: 18px;
  margin-bottom: 10px;
  margin-top: 10px;
  display: block;
}
.step-item text {
  display: block;
  font-size: 18px;
  line-height: 30px;
}
.el-dialog__body {
  max-height: calc(100vh - 200px);
  overflow-y: auto;
  padding: 10px 20px;
  box-sizing: border-box;
}
</style>