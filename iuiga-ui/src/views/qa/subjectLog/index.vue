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
      <el-form-item label="开始时间" prop="beginTime">
        <el-date-picker
          type="datetime"
          value-format="yyyy-MM-dd HH:mm:ss"
          v-model="queryParams.beginTime"
          placeholder="请输入开始时间"
          clearable
          size="small"
          @change="handleQuery"
        />
      </el-form-item>
      <el-form-item label="结束时间" prop="endTime">
        <el-date-picker
          type="datetime"
          value-format="yyyy-MM-dd HH:mm:ss"
          v-model="queryParams.endTime"
          placeholder="请输入结束时间"
          clearable
          size="small"
          @change="handleQuery"
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

    <el-table
      v-loading="loading"
      :data="dataList"
      @selection-change="handleSelectionChange"
    >
      <el-table-column
        label="序号"
        align="center"
        prop="subjectLogId"
        width="100"
      />
      <el-table-column
        label="问答标题"
        align="center"
        prop="title"
        :show-overflow-tooltip="true"
      />
      <el-table-column label="问答版本" align="center" prop="subjectLogTitle" />
      <el-table-column label="问答用户" align="center" prop="subjectLogTitle" />
      <el-table-column label="得分" align="center" prop="score" />
      <el-table-column
        label="创建者"
        align="center"
        prop="createBy"
      />
      <el-table-column
        label="创建时间"
        align="center"
        prop="createTime"
      >
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime, "{y}-{m}-{d}") }}</span>
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
  </div>
</template>

<script>
import { listSubjectLog, getSubjectLog } from "@/api/qa/subjectLog";

export default {
  name: "SubjectLog",
  dicts: ["sys_subjectLog_status", "sys_subjectLog_type"],
  data() {
    return {
      // 遮罩层
      loading: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 问答日志表格数据
      dataList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        title: undefined,
        beginTime: undefined,
        endTime: undefined,
      },
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询问答日志列表 */
    getList() {
      this.loading = true;
      listSubjectLog(this.queryParams).then((response) => {
        this.subjectLogList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        subjectLogId: undefined,
        subjectLogTitle: undefined,
        subjectLogType: undefined,
        subjectLogContent: undefined,
        status: "0",
      };
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
  },
};
</script>
