<template>
  <div style="display:inline-block;margin-left: 10px;">
    <el-button :type="type" :size="size" :icon="icon" @click="handleRun">{{ label }}</el-button>
    <el-dialog
      :title="form.title"
      :visible.sync="form.open"
      width="600px"
      append-to-body
    >
      <el-col v-if="form.count == 0">
        <el-row>
          <el-col class="subject-title">{{ form.data.title }}</el-col>
          <el-col class="subject-version">版本：{{ form.data.version }}</el-col>
        </el-row>
        <el-row class="subject-content">{{ form.data.content }}</el-row>
      </el-col>
      <el-col v-else-if="!form.complete">
        <el-row>
          <el-col class="subject-question">{{
            form.count + ". " + form.nowQuestion.nextQuestion
          }}</el-col>
        </el-row>
        <el-row>
          <el-radio-group v-model="form.nowId">
            <el-col
              :span="12"
              v-for="item in form.nowQuestion.children"
              :key="item.subjectInfoId"
              class="subject-answer"
            >
              <el-radio :label="item.subjectInfoId">{{
                item.lastAnswer
              }}</el-radio>
            </el-col>
          </el-radio-group>
        </el-row>
      </el-col>
      <el-col v-else> 恭喜你答题完成 </el-col>
      <div slot="footer" class="dialog-footer">
        <el-button v-if="form.count == 0" type="primary" @click="start"
          >开始答题</el-button
        >
        <el-button v-if="form.count == 0 || form.complete" @click="cancel">关 闭</el-button>
        <el-button
          v-if="form.count > 0 && !form.complete"
          :disabled="form.nowId == undefined"
          type="success"
          @click="next"
          >下一题</el-button
        >
      </div>
    </el-dialog>
  </div>
</template>
<script>
import { getSubject } from "@/api/qa/subject";

export default {
  name: "SubjectDemo",
  props: {
    type: {
      type: String,
      default: "text",
    },
    subjectId: {
      type: Number,
      default: -1,
    },
    label: {
      type: String,
      default: "试用",
    },
    size: {
      type: String,
      default: "small",
    },
    icon: {
      type: String
    },
  },
  data() {
    return {
      form: {
        title: "",
        open: false,
        data: {},
        count: 0,
        score: 0,
        questionArr: [],
        answerArr: [],
        lastId: undefined,
        nowId: undefined,
        nowQuestion: {},
        complete: false,
      },
    };
  },
  created() {},
  methods: {
    /** 获取问答详情 */
    getSubject() {
      if (this.subjectId > 0) {
        getSubject(this.subjectId).then((response) => {
          this.form.data = response.data;
        });
      } else {
        this.$modal.msgError("请选择问答");
      }
    },
    /** 运行 */
    handleRun() {
      this.reset();
      this.form.title = "Demo";
      this.form.open = true;
      this.getSubject();
    },
    /** 重置表单 */
    reset() {
      this.form.data = {};
      this.form.count = 0;
      this.form.score = 0;
      this.form.questionArr = [];
      this.form.answerArr = [];
      this.form.nowId = undefined;
      this.form.lastId = undefined;
      this.form.complete = false;
      this.form.nowQuestion = {
        subjectId: undefined,
        subjectInfoId: undefined,
        parentId: undefined,
        lastAnswer: undefined,
        nextQuestion: undefined,
        children: [],
      };
    },
    /** 关闭 */
    cancel() {
      this.form.open = false;
    },
    /** 开始 */
    start() {
      let question = this.findNext(
        this.form.data.infoTreeList,
        this.form.nowId
      );
      if (question != undefined) {
        this.form.nowQuestion = question;
        this.form.count = this.form.count + 1;
        this.form.lastId = this.form.nowId;
        this.form.nowId = undefined;
      } else {
        if(this.form.count == 0) {
          this.$modal.msgError("本问答无题，请添加题目后试用！");
          return;
        }
        this.form.complete = true;
      }
    },
    /** 继续 */
    next() {
      this.form.score =
        this.form.score +
        this.form.nowQuestion.children.filter(
          (item) => item.subjectInfoId == this.form.nowId
        )[0].score;
      this.form.questionArr.push(this.form.nowQuestion);
      this.form.answerArr.push(this.nowId);
      this.start();
    },
    /** 查询下一题 */
    findNext(list, nowId) {
      let nextItem = undefined;
      list.some((item) => {
        if (
          (nowId == undefined || item.subjectInfoId == nowId) &&
          item.nextQuestion != undefined &&
          item.children != undefined &&
          item.children.length > 0
        ) {
          nextItem = item;
          return true;
        } else {
          let nextChildItem = this.findNext(item.children, nowId);
          if (nextChildItem != undefined) {
            nextItem = nextChildItem;
            return true;
          }
        }
      });
      return nextItem;
    },
  },
};
</script>
<style scoped>
.subject-title {
  font-size: 30px;
  text-align: center;
}
.subject-version {
  font-size: 15px;
  text-align: center;
}
.subject-content {
  font-size: 20px;
  text-align: left;
}
.subject-question {
  font-size: 20px;
  text-align: left;
}
.subject-answer {
  font-size: 20px;
  text-align: left;
}
</style>
