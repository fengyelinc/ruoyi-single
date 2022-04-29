<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryParam" inline>
      <el-form-item label="消息编码" prop="messageCode">
        <el-input
          v-model="queryParams.messageCode"
          placeholder="消息编码"
          clearable
          size="small"
          style="width: 240px"
          @keyup.enter.native="handleQuery"
        ></el-input>
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
          v-hasPermi="['system:message:add']"
          >新增</el-button
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
          v-hasPermi="['system:message:remove']"
          >删除</el-button
        >
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:message:export']"
          >导出</el-button
        >
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-refresh"
          size="mini"
          @click="handleRefreshCache"
          v-hasPermi="['system:message:refresh']"
        >刷新缓存</el-button>
      </el-col>
      <right-toolbar
        :showSearch.sync="showSearch"
        @queryTable="getList"
      ></right-toolbar>
    </el-row>

    <el-table :loading="loading" :data="dataList" row-key="messageId">
      <el-table-column type="selection"></el-table-column>
      <el-table-column
        label="消息编码"
        align="center"
        prop="messageCode"
      ></el-table-column>
      <el-table-column
        label="消息分组"
        align="center"
        prop="messageGroup"
      ></el-table-column>
      <el-table-column
        label="消息内容"
        align="center"
        show-overflow-tooltip
        prop="messageContent"
      ></el-table-column>
      <el-table-column
        label="备注"
        align="center"
        show-overflow-tooltip
        prop="remark"
      ></el-table-column>
      <el-table-column label="创建时间" prop="createTime">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作">
        <template slot-scope="scope">
          <el-button type="text" v-hasPermi="['system:message:query']" @click="getInfo(scope.row)">查看</el-button>
          <el-button type="text" v-hasPermi="['system:message:edit']" @click="handleEdit(scope.row)">编辑</el-button>
          <el-button type="text" v-hasPermi="['system:message:remove']" @click="handleDelete(scope.row)"
            >删除</el-button
          >
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
    <el-dialog
      :title="form.title"
      :visible.sync="form.open"
      width="500px"
      append-to-body
    >
      <el-form ref="form" :model="form" :rules="form.rules" label-width="100px">
        <el-form-item label="消息编码" prop="messageCode">
          <el-input
            v-model="form.data.messageCode"
            placeholder="消息编码"
          ></el-input>
        </el-form-item>
        <el-form-item label="消息分组" prop="messageGroup">
          <el-input
            v-model="form.data.messageGroup"
            placeholder="消息分组"
          ></el-input>
        </el-form-item>
        <el-form-item label="消息内容" prop="messageContent">
          <el-input
            v-model="form.data.messageContent"
            placeholder="消息内容"
          ></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
import {
  listMessage,
  getMessage,
  addMessage,
  updateMessage,
  deleteMeesage,
  refreshMessage
} from "@/api/system/message";

export default {
  name: "Message",
  data() {
    return {
      // 遮罩层
      loading: false,
      // 选中ID
      ids: [],
      // 查询条件
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        messageCode: undefined,
      },
      // 总条数
      total: 0,
      // 列表数据
      dataList: [],
      // 表单信息
      form: {
        open: false,
        title: "",
        type: "",
        rules: {
          messageCode: [
            { required: true, message: "消息编码不能为空", trigger: "blur" },
          ],
          messageGroup: [
            { required: true, message: "消息分组不能为空", trigger: "blur" },
          ],
          messageContent: [
            { required: true, message: "消息内容不能为空", trigger: "blur" },
          ],
        },
        data: {
          messageId: undefined,
          messageCode: undefined,
          messageGroup: undefined,
          messageContent: undefined,
        },
      },
    };
  },
  created() {
    this.getList();
  },
  methods: {
    // 查询列表
    getList() {
      this.loading = true;
      listMessage(this.queryParams).then((response) => {
        this.dataList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 查询详情
    handleQuery() {
      this.getList();
    },
    // 重置表单
    reset() {
      this.form.data = {
        messageId: undefined,
        messageCode: undefined,
        messageGroup: undefined,
        messageContent: undefined,
      };
      this.resetForm("form");
    },
    // 关闭表单
    cancel() {
      this.form.open = false;
      this.reset();
    },
    // 查询
    getInfo(row) {
      this.reset();
      getMessage(row.messageId).then((response) => {
        this.form.data = response.data;
        this.form.title = "查看";
        this.form.type = "detail";
        this.form.open = true;
      });
    },
    // 新增
    handleAdd() {
      this.reset();
      this.form.title = "新增";
      this.form.type = "add";
      this.form.open = true;
    },
    // 编辑
    handleEdit(row) {
      this.reset();
      getMessage(row.messageId).then((response) => {
        this.form.data = response.data;
        this.form.title = "编辑";
        this.form.type = "edit";
        this.form.open = true;
      });
    },
    // 提交表单
    submitForm() {
      this.$refs.form.validate((valid) => {
        if (valid) {
          if (this.form.type == "add") {
            addMessage(this.form.data).then((response) => {
              this.$modal.msgSuccess("新增成功");
            });
          } else if (this.form.type == "edit") {
            updateMessage(this.form.data).then((response) => {
              this.$modal.msgSuccess("编辑成功");
            });
          }
        }
      });
    },
    // 删除
    handleDelete(row) {
      let idStr = "";
      if (row) {
        idStr = row.messageId;
      } else {
        ids = this.$refs.message.selection.map((item) => item.messageId);
      }
      if (idStr == "" || idStr == undefined) {
        this.$modal.msgError("请选择需要删除的消息");
        return;
      }
      this.$confirm("确定删除选中的消息吗？", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          return deleteMeesage();
        })
        .then((response) => {
          this.msgSuccess("删除成功");
        })
        .catch(() => {});
    },
    /** 刷新缓存按钮操作 */
    handleRefreshCache() {
      refreshMessage().then(() => {
        this.$modal.msgSuccess("刷新成功");
      });
    }
  },
};
</script>
