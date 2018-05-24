const envList = [
  {
    id: 1,
    name: 'dev',
    description: '开发',
    createBy: '曲丽丽',
    updateBy: '林东东',
    createTime: '2018-05-17 14:18:40',
    updateTime: '2018-05-18 04:18:40',
  },
  {
    id: 1,
    name: 'production',
    description: '生产环境',
    createBy: '曲丽丽',
    updateBy: '林东东',
    createTime: '2018-05-17 14:18:40',
    updateTime: '2018-05-18 04:18:40',
  },
];

export function findEnvList(req, res) {
  res.json(envList);
}

export default {
  findEnvList,
};
