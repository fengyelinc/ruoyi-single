<template>
  <div class="app-container">
    <el-form
      :model="queryParams"
      ref="queryForm"
      :inline="true"
      v-show="showSearch"
      label-width="68px"
    >
      <el-form-item label="视频标题" prop="title">
        <el-input
          v-model="queryParams.title"
          placeholder="请输入视频标题"
          clearable
          size="small"
          style="width: 240px"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="视频简介" prop="description">
        <el-input
          v-model="queryParams.description"
          placeholder="请输入视频简介"
          clearable
          size="small"
          style="width: 240px"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="视频状态" prop="status">
        <el-select
          v-model="queryParams.status"
          placeholder="视频状态"
          clearable
          size="small"
        >
          <el-option
            v-for="dict in dict.type.sys_normal_disable"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
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
          v-hasPermi="['vms:video:add']"
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
          v-hasPermi="['vms:video:edit']"
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
          v-hasPermi="['vms:video:remove']"
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
      :data="videoList"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="视频ID" align="center" prop="videoId" />
      <el-table-column label="视频标识" align="center" prop="videoUuid" />
      <el-table-column
        label="视频标题"
        align="center"
        prop="title"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="视频简介"
        align="center"
        prop="description"
        :show-overflow-tooltip="true"
      />
      <el-table-column label="视频作者" align="center" prop="author" />
      <el-table-column label="视频状态" align="center" prop="status">
        <template slot-scope="scope">
          <dict-tag
            :options="dict.type.sys_normal_disable"
            :value="scope.row.status"
          />
        </template>
      </el-table-column>
      <el-table-column
        label="备注"
        align="center"
        prop="remark"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="创建者"
        align="center"
        prop="createBy"
        width="100"
      />
      <el-table-column
        label="创建时间"
        align="center"
        prop="createTime"
        width="180"
      >
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
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
            v-hasPermi="['vms:video:edit']"
            >修改</el-button
          >
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['vms:video:remove']"
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

    <!-- 添加或修改视频配置对话框 -->
    <el-dialog
      :title="form.title"
      :visible.sync="form.open"
      width="700px"
      append-to-body
    >
      <el-form
        ref="form"
        :model="form.data"
        :rules="form.rules"
        label-width="80px"
      >
        <el-divider content-position="left">基础数据</el-divider>
        <el-row>
          <el-col :span="24">
            <el-form-item
              v-if="form.data.videoId != undefined"
              label="视频标识"
              prop="videoUuid"
            >
              <el-input disabled v-model="form.data.videoUuid" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="视频标题" prop="title">
              <el-input
                v-model="form.data.title"
                placeholder="请输入视频标题"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="视频简介" prop="description">
              <el-input
                v-model="form.data.description"
                placeholder="请输入视频简介"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="视频作者" prop="author">
              <el-input
                v-model="form.data.author"
                placeholder="请输入视频作者"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="视频状态" prop="status">
              <el-radio-group v-model="form.data.status">
                <el-radio
                  v-for="dict in dict.type.sys_normal_disable"
                  :key="dict.value"
                  :label="dict.value"
                  >{{ dict.label }}</el-radio
                >
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="备注" prop="remark">
              <el-input
                v-model="form.data.remark"
                type="textarea"
                placeholder="请输入内容"
              />
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-row>
          <el-col :span="12">
          <el-divider content-position="left">封面数据</el-divider>
          <el-upload
              ref="coverUpload"
              :limit="1"
              accept=".jpg"
              :headers="form.coverUpload.headers"
              :action="form.coverUpload.url"
              :disabled="form.coverUpload.isUploading"
              :on-progress="handleCoverUploadProgress"
              :on-success="handleCoverSuccess"
              :auto-upload="true"
            >
              <el-button size="small" type="primary">点击上传</el-button>
              <el-button v-if="form.data.videoId!=undefined" size="small" type="success" @click="handleDownloadCover">查看封面</el-button>
              <div slot="tip" class="el-upload__tip">
                <span>仅允许导入jpg格式文件。</span>
              </div>
            </el-upload>
          </el-col>
          <el-col :span="12">
          <el-divider content-position="left">视频数据</el-divider>
            <el-upload
              ref="videoUpload"
              :limit="1"
              accept=".mp4"
              :headers="form.videoUpload.headers"
              :action="form.videoUpload.url"
              :disabled="form.videoUpload.isUploading"
              :on-progress="handleVideoUploadProgress"
              :on-success="handleVideoSuccess"
              :auto-upload="true"
            >
              <el-button size="small" type="primary">点击上传</el-button>
              <el-button v-if="form.data.videoId!=undefined" size="small" type="success" @click="handleDownloadVideo">查看视频</el-button>
              <div slot="tip" class="el-upload__tip">
                <span>仅允许导入mp4格式文件。</span>
              </div>
            </el-upload>
          </el-col>
        </el-row>
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
  listVideo,
  getVideo,
  delVideo,
  addVideo,
  updateVideo,
} from "@/api/vms/video";
import { getToken } from "@/utils/auth";

export default {
  name: "Video",
  dicts: ["sys_normal_disable"],
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
      // 视频表格数据
      videoList: [],
      // 查询视频
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        title: undefined,
        description: undefined,
        status: undefined,
      },
      // 表单视频
      form: {
        // 弹出层标题
        title: "",
        // 是否显示弹出层
        open: false,
        // 表单校验
        rules: {
          title: [
            { required: true, message: "视频标题不能为空", trigger: "blur" },
          ],
          description: [
            { required: true, message: "视频简介不能为空", trigger: "blur" },
          ],
          author: [
            { required: true, message: "视频作者不能为空", trigger: "blur" },
          ],
        },
        // 封面上传参数
        coverUpload: {
          // 是否禁用上传
          isUploading: false,
          // 设置上传的请求头部
          headers: { Authorization: "Bearer " + getToken() },
          // 上传的地址
          url: process.env.VUE_APP_BASE_API + "/vms/video/coverUpload",
        },
        // 视频上传参数
        videoUpload: {
          // 是否禁用上传
          isUploading: false,
          // 设置上传的请求头部
          headers: { Authorization: "Bearer " + getToken() },
          // 上传的地址
          url: process.env.VUE_APP_BASE_API + "/vms/video/videoUpload",
        },
        data: {},
      },
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询视频列表 */
    getList() {
      this.loading = true;
      listVideo(this.queryParams).then((response) => {
        this.videoList = response.rows;
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
        videoId: undefined,
        videoUuid: undefined,
        title: undefined,
        description: undefined,
        author: undefined,
        status: "0",
        remark: undefined,
        videoUrl: undefined,
        videoPath: undefined,
        coverUrl: undefined,
        coverPath: undefined
      };
      this.resetForm("form");
      if (this.$refs.videoUpload != undefined) {
        this.$refs.videoUpload.clearFiles();
      }
      if (this.$refs.coverUpload != undefined) {
        this.$refs.coverUpload.clearFiles();
      }
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
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.form.open = true;
      this.form.title = "添加视频";
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map((item) => item.videoId);
      this.single = selection.length != 1;
      this.multiple = !selection.length;
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const videoId = row.videoId || this.ids;
      getVideo(videoId).then((response) => {
        this.form.data = response.data;
        this.form.open = true;
        this.form.title = "修改视频";
      });
    },
    /** 提交按钮 */
    submitForm: function () {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          if (this.form.data.videoId != undefined) {
            updateVideo(this.form.data).then((response) => {
              this.$modal.msgSuccess("修改成功");
              this.form.open = false;
              this.getList();
            });
          } else {
            addVideo(this.form.data).then((response) => {
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
      const videoIds = row.videoId || this.ids;
      this.$modal
        .confirm('是否确认删除视频编号为"' + videoIds + '"的数据项？')
        .then(function () {
          return delVideo(videoIds);
        })
        .then(() => {
          this.getList();
          this.$modal.msgSuccess("删除成功");
        })
        .catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      let params = JSON.parse(JSON.stringify(this.queryParams));
      params = {
        exportIdList: this.ids,
        ...params
      }
      this.download(
        "vms/video/export",
        params,
        `video_${new Date().getTime()}.xlsx`
      );
    },
    // 视频上传中处理
    handleVideoUploadProgress(event, file, fileList) {
      this.form.videoUpload.isUploading = true;
    },
    // 视频上传成功处理
    handleVideoSuccess(response, file, fileList) {
      this.form.data.videoUrl = response.url;
      this.form.data.videoPath = response.path;
      this.form.videoUpload.isUploading = false;
    },
    // 视频下载
    handleDownloadVideo() {
      window.open(this.form.data.videoUrl, "查看视频");
    },
    // 封面上传中处理
    handleCoverUploadProgress(event, file, fileList) {
      this.form.coverUpload.isUploading = true;
    },
    // 封面上传成功处理
    handleCoverSuccess(response, file, fileList) {
      this.form.data.coverUrl = response.url;
      this.form.data.coverPath = response.path;
      this.form.coverUpload.isUploading = false;
    },
    // 视频下载
    handleDownloadCover() {
      window.open(this.form.data.coverUrl, "查看图片");
    },
  },
};
</script>
