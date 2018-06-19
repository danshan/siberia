import React, { PureComponent } from 'react';
import { Card, Button, Form, Icon, Popover } from 'antd';
import { connect } from 'dva';
import FooterToolbar from 'components/FooterToolbar';
import PageHeaderLayout from '../../layouts/PageHeaderLayout';
import EnvironmentForm from './EnvironmentForm';
import AppForm from './AppForm';
import styles from './style.less';

const fieldLabels = {
  name: '仓库名',
  url: '仓库域名',
  owner: '仓库管理员',
  approver: '审批人',
  dateRange: '生效日期',
  type: '仓库类型',
  name2: '任务名',
  url2: '任务描述',
  owner2: '执行人',
  approver2: '责任人',
  dateRange2: '生效日期',
  type2: '任务类型',
};

class Settings extends PureComponent {
  state = {
    width: '100%',
  };

  componentDidMount() {
    window.addEventListener('resize', this.resizeFooterToolbar);

    this.jumpToAnchor();

    this.paginateEnvList();
    this.paginateAppList();
  }

  componentWillUnmount() {
    window.removeEventListener('resize', this.resizeFooterToolbar);
  }

  jumpToAnchor = () => {
    const anchorName = this.props.location.hash;
    const scrollToAnchor = () => {
      const hash = anchorName.replace('#', '');
      if (hash) {
        document.getElementsByName(hash)[0].scrollIntoView();
      }
    };
    scrollToAnchor();
    window.onhashchange = scrollToAnchor;
  };

  paginateEnvList = () => {
    this.props.dispatch({
      type: 'settings/paginateEnvList',
      payload: {
        count: 5,
      },
    });
  };

  paginateAppList = () => {
    this.props.dispatch({
      type: 'settings/paginateAppList',
      payload: {
        count: 5,
      },
    });
  };

  resizeFooterToolbar = () => {
    const sider = document.querySelectorAll('.ant-layout-sider')[0];
    const width = `calc(100% - ${sider.style.width})`;
    if (this.state.width !== width) {
      this.setState({ width });
    }
  };

  createEnv = env => {
    this.props.dispatch({
      type: 'settings/createEnv',
      payload: env,
    });
  };

  render() {
    const { settings: { envList, appList }, form, dispatch, submitting } = this.props;
    const { validateFieldsAndScroll, getFieldsError } = form;
    const validate = () => {
      validateFieldsAndScroll((error, values) => {
        if (!error) {
          // submit the values
          dispatch({
            type: 'form/submitAdvancedForm',
            payload: values,
          });
        }
      });
    };
    const errors = getFieldsError();
    const getErrorInfo = () => {
      const errorCount = Object.keys(errors).filter(key => errors[key]).length;
      if (!errors || errorCount === 0) {
        return null;
      }
      const scrollToField = fieldKey => {
        const labelNode = document.querySelector(`label[for="${fieldKey}"]`);
        if (labelNode) {
          labelNode.scrollIntoView(true);
        }
      };
      const errorList = Object.keys(errors).map(key => {
        if (!errors[key]) {
          return null;
        }
        return (
          <li key={key} className={styles.errorListItem} onClick={() => scrollToField(key)}>
            <Icon type="cross-circle-o" className={styles.errorIcon} />
            <div className={styles.errorMessage}>{errors[key][0]}</div>
            <div className={styles.errorField}>{fieldLabels[key]}</div>
          </li>
        );
      });
      return (
        <span className={styles.errorIcon}>
          <Popover
            title="表单校验信息"
            content={errorList}
            overlayClassName={styles.errorPopover}
            trigger="click"
            getPopupContainer={trigger => trigger.parentNode}
          >
            <Icon type="exclamation-circle" />
          </Popover>
          {errorCount}
        </span>
      );
    };
    return (
      <PageHeaderLayout title="管理后台" content="管理后台" wrapperClassName={styles.advancedForm}>
        <Card name="environment" title="环境管理" className={styles.card} bordered={false}>
          <EnvironmentForm envList={envList} createEnv={this.createEnv} />
        </Card>

        <Card name="app" title="应用配置" className={styles.card} bordered={false}>
          <AppForm envList={appList} createEnv={this.createEnv} />
        </Card>

        <FooterToolbar style={{ width: this.state.width }}>
          {getErrorInfo()}
          <Button type="primary" onClick={validate} loading={submitting}>
            提交
          </Button>
        </FooterToolbar>
      </PageHeaderLayout>
    );
  }
}

export default connect(({ settings, global, loading }) => ({
  settings,
  collapsed: global.collapsed,
  submitting: loading.effects['form/submitAdvancedForm'],
}))(Form.create()(Settings));
