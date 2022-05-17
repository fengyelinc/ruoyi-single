<template>
  <div class="app-container">
    <el-form
      :model="queryParams"
      ref="queryForm"
      :inline="true"
      v-show="showSearch"
      label-width="68px"
    >
      <el-form-item label="问答标题" prop="title">
        <el-input
          v-model="queryParams.title"
          placeholder="请输入问答标题"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button
          type="primary"
          icon="el-icon-search"
          size="mini"
          @click="handleQuery"
          >搜索</el-button
        >
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery"
          >重置</el-button
        >
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['qa:subject:add']"
          >新增</el-button
        >
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['qa:subject:edit']"
          >修改</el-button
        >
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['qa:subject:remove']"
          >删除</el-button
        >
      </el-col>
      <right-toolbar
        :showSearch.sync="showSearch"
        @queryTable="getList"
      ></right-toolbar>
    </el-row>

    <el-table
      v-loading="loading"
      :data="subjectList"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column
        label="序号"
        align="center"
        prop="subjectId"
        width="100"
      />
      <el-table-column
        label="问答标题"
        align="center"
        prop="title"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="问答唯一标识"
        align="center"
        prop="subjectUuid"
        show-overflow-tooltip
      />
      <el-table-column label="问答类型" align="center" prop="type">
        <template slot-scope="scope">
          <dict-tag
            :options="dict.type.qa_subject_type"
            :value="scope.row.type"
          />
        </template>
      </el-table-column>
      <el-table-column label="创建者" align="center" prop="createBy" />
      <el-table-column label="创建时间" align="center">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime, "{y}-{m}-{d}") }}</span>
        </template>
      </el-table-column>
      <el-table-column
        label="操作"
        align="center"
        class-name="small-padding fixed-width"
      >
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['qa:subject:edit']"
            >修改</el-button
          >
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['qa:subject:remove']"
            >删除</el-button
          >
          <subject-demo
            icon="el-icon-grape"
            type="text"
            size="mini"
            :subjectId="scope.row.subjectId"
          ></subject-demo>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total > 0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改问答对话框 -->
    <el-dialog
      :title="form.title"
      :visible.sync="form.open"
      width="780px"
      append-to-body
    >
      <el-tabs v-model="form.activeName" accordion>
        <el-tab-pane label="通用数据" name="1">
          <el-form
            ref="form"
            :model="form.data"
            :rules="form.rules"
            label-width="80px"
          >
            <el-row>
              <el-col :span="12">
                <el-form-item label="问答标题" prop="title">
                  <el-input
                    v-model="form.data.title"
                    placeholder="请输入问答标题"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="问答版本" prop="version">
                  <el-input
                    v-model="form.data.version"
                    placeholder="请输入问答版本"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="问答类型" prop="type">
                  <el-select v-model="form.data.type" placeholder="请选择">
                    <el-option
                      v-for="dict in dict.type.qa_subject_type"
                      :key="dict.value"
                      :label="dict.label"
                      :value="dict.value"
                    ></el-option>
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="24">
                <el-form-item label="问答内容">
                  <el-input
                    type="textarea"
                    v-model="form.data.content"
                    placeholder="请输入问答内容"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="24">
                <el-form-item label="问答备注">
                  <el-input
                    type="textarea"
                    v-model="form.data.remark"
                    placeholder="请输入问答备注"
                  />
                </el-form-item>
              </el-col>
            </el-row>
          </el-form>
        </el-tab-pane>
        <el-tab-pane v-if="form.data.subjectId" label="问答数据" name="2">
          <el-form
            ref="formInfo"
            :model="form.infoData"
            :rules="form.rules"
            label-width="80px"
          >
            <el-col :span="12">
              <el-button
                :disabled="form.checkNodesQty != 1"
                type="primary"
                size="mini"
                @click="() => handleAppendFormInfoData()"
              >
                新增
              </el-button>
              <el-button
                :disabled="form.checkNodesQty != 1"
                type="success"
                size="mini"
                @click="() => handleInfoTreeClick()"
              >
                编辑
              </el-button>
              <el-button
                :disabled="
                  form.checkNodesQty == undefined || form.checkNodesQty == 0
                "
                type="danger"
                size="mini"
                @click="() => handleRemoveFormInfoData()"
              >
                删除
              </el-button>
              <el-tree
                ref="infoTree"
                class="tree-border"
                show-checkbox
                check-strictly
                :data="form.data.infoTreeList"
                node-key="subjectInfoId"
                :props="subjectInfoProps"
                @check-change="handleFormInfoTreeCheck"
              >
              </el-tree>
            </el-col>
            <el-col :span="12">
              <el-form-item
                v-if="
                  form.infoData.parentId != undefined &&
                  form.infoData.parentId > 0
                "
                label="上题答案"
                prop="lastAnswer"
              >
                <el-input
                  v-model="form.infoData.lastAnswer"
                  placeholder="请输入上题答案"
                />
              </el-form-item>
              <el-form-item
                v-if="
                  form.infoData.parentId != undefined &&
                  form.infoData.parentId > 0
                "
                label="上题得分"
                prop="score"
              >
                <el-input-number
                  v-model="form.infoData.score"
                  placeholder="请输入上题得分"
                />
              </el-form-item>
              <el-form-item
                label="当前问题"
                :rules="
                  form.infoData.parentId != undefined &&
                  form.infoData.parentId > 0
                    ? []
                    : form.rules.nextQuestion
                "
                prop="nextQuestion"
              >
                <el-input
                  v-model="form.infoData.nextQuestion"
                  placeholder="请输入当前问题"
                />
              </el-form-item>
              <el-form-item label="备注" prop="remark">
                <el-input
                  v-model="form.infoData.remark"
                  placeholder="请输入备注"
                />
              </el-form-item>
              <el-form-item>
                <el-button
                  type="primary"
                  v-if="form.infoData.subjectInfoId == undefined"
                  @click="handleAddFormInfoData"
                  v-hasPermi="['qa:subject:edit']"
                  >新增</el-button
                >
                <el-button
                  type="primary"
                  v-if="form.infoData.subjectInfoId != undefined"
                  @click="handleUpdateFormInfoData"
                  v-hasPermi="['qa:subject:edit']"
                  >保存</el-button
                >
                <el-button
                  type="danger"
                  v-if="form.infoData.subjectInfoId != undefined"
                  @click="handleRemoveFormInfoData"
                  v-hasPermi="['qa:subject:edit']"
                  >删除</el-button
                >
              </el-form-item>
            </el-col>
          </el-form>
        </el-tab-pane>
      </el-tabs>

      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  listSubject,
  getSubject,
  delSubject,
  addSubject,
  updateSubject,
  delSubjectData,
  addSubjectData,
  updateSubjectData,
} from "@/api/qa/subject";

import SubjectDemo from "@/views/qa/subject/components/SubjectDemo";

export default {
  name: "Subject",
  dicts: ["qa_subject_type"],
  components: { SubjectDemo },
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 问答表格数据
      subjectList: [],
      // 问答树状格式
      subjectInfoProps: {
        children: "children",
        label: "nextQuestion",
      },
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        title: undefined,
      },
      // 表单参数
      form: {
        // 弹出层标题
        title: "",
        // 是否显示弹出层
        open: false,
        // 表单名
        activeName: "1",
        // 表单校验
        rules: {
          title: [
            { required: true, message: "问答标题不能为空", trigger: "blur" },
          ],
          version: [
            { required: true, message: "问答版本不能为空", trigger: "blur" },
          ],
          type: [
            { required: true, message: "问答类型不能为空", trigger: "change" },
          ],
          lastAnswer: [
            { required: true, message: "上题答案不能为空", trigger: "blur" },
          ],
          nextQuestion: [
            { required: true, message: "问题内容不能为空", trigger: "blur" },
          ],
        },
        checkNodesQty: 0,
        // 数据
        data: {},
        // 问答内容
        infoData: {},
      },
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询问答列表 */
    getList() {
      this.loading = true;
      listSubject(this.queryParams).then((response) => {
        this.subjectList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.form.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form.data = {
        subjectId: undefined,
        title: undefined,
        type: undefined,
        content: undefined,
        remark: undefined,
      };
      this.form.infoData = {
        subjectInfoId: undefined,
        subjectId: undefined,
        parentId: -1,
        lastAnswer: undefined,
        nextQuestion: undefined,
        score: 0,
        remark: undefined,
      };
      this.form.checkNodesQty = 0;
      this.form.activeName = "1";
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map((item) => item.subjectId);
      this.single = selection.length != 1;
      this.multiple = !selection.length;
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.form.open = true;
      this.form.title = "添加问答";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const subjectId = row.subjectId || this.ids;
      getSubject(subjectId).then((response) => {
        this.form.data = response.data;
        this.form.open = true;
        this.form.title = "修改问答";
      });
    },
    /** 提交按钮 */
    submitForm: function () {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          if (this.form.data.subjectId != undefined) {
            updateSubject(this.form.data).then((response) => {
              this.$modal.msgSuccess("修改成功");
              this.form.open = false;
              this.getList();
            });
          } else {
            addSubject(this.form.data).then((response) => {
              this.$modal.msgSuccess("新增成功");
              this.form.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const subjectIds = row.subjectId || this.ids;
      this.$modal
        .confirm('是否确认删除问答编号为"' + subjectIds + '"的数据项？')
        .then(function () {
          return delSubject(subjectIds);
        })
        .then(() => {
          this.getList();
          this.$modal.msgSuccess("删除成功");
        })
        .catch(() => {});
    },
    /** 刷新表单数据内容 */
    resetFormInfoData() {
      this.form.infoData = {
        subjectInfoId: undefined,
        subjectId: undefined,
        parentId: -1,
        lastAnswer: undefined,
        nextQuestion: undefined,
        score: 0,
        remark: undefined,
      };
      this.form.checkNodesQty = 0;
      this.resetForm("formInfo");
    },
    /** 点击树状图 */
    handleInfoTreeClick() {
      let data = this.$refs.infoTree.getCheckedNodes()[0];
      this.form.infoData = JSON.parse(JSON.stringify(data));
    },
    /** 刷新表单内容 */
    handleFormInfoQuery() {
      this.resetFormInfoData();
      getSubject(this.form.data.subjectId).then((response) => {
        this.form.data = response.data;
      });
    },
    /** 问题表格树状图点击 */
    handleFormInfoTreeCheck() {
      this.form.checkNodesQty = this.$refs.infoTree.getCheckedNodes().length;
    },
    /** 添加问答数据 */
    handleAddFormInfoData() {
      this.form.infoData.subjectId = this.form.data.subjectId;
      addSubjectData(this.form.infoData).then((response) => {
        this.$modal.msgSuccess("新增成功");
        this.handleFormInfoQuery();
      });
    },
    /** 保存问答数据 */
    handleUpdateFormInfoData() {
      updateSubjectData(this.form.infoData).then((response) => {
        this.$modal.msgSuccess("保存成功");
        this.handleFormInfoQuery();
      });
    },
    /** 新增问答子数据 */
    handleAppendFormInfoData() {
      let data = this.$refs.infoTree.getCheckedNodes()[0];
      this.resetFormInfoData();
      this.form.infoData.subjectId = data.subjectId;
      this.form.infoData.parentId = data.subjectInfoId;
    },
    /** 删除问答及子数据数据 */
    handleRemoveFormInfoData() {
      let data = this.$refs.infoTree.getCheckedNodes()[0];
      let id =
        data != undefined
          ? data.subjectInfoId
          : this.form.infoData.subjectInfoId;
      this.$confirm("确定删除当前问题节点及其子集吗？", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          return delSubjectData(id);
        })
        .then((response) => {
          this.$modal.msgSuccess("删除成功");
          this.handleFormInfoQuery();
        })
        .catch(() => {});
    },
  },
};
</script>
<style>
.custom-tree-node {
  display: flex;
  align-items: center;
  justify-content: space-between;
  font-size: 14px;
  padding-right: 8px;
}
.subject-tree-node-label {
  white-space: nowrap;
  text-overflow: ellipsis;
  width: 200px;
  overflow: hidden;
}
</style>
