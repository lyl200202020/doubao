const axios = require('axios');

const BASE_URL = 'http://localhost:8080';

// 测试配置
const config = {
  username: 'admin',
  password: 'admin123'
};

let authToken = '';

// 创建 axios 实例
const api = axios.create({
  baseURL: BASE_URL,
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json'
  }
});

// 请求拦截器
api.interceptors.request.use(config => {
  if (authToken) {
    config.headers.Authorization = `Bearer ${authToken}`;
  }
  return config;
});

// 响应拦截器
api.interceptors.response.use(
  response => response.data,
  error => {
    console.error('API Error:', error.response?.data || error.message);
    throw error;
  }
);

// 测试函数
async function testLogin() {
  console.log('\n======================================');
  console.log('1. 测试登录接口');
  console.log('======================================');
  
  try {
    const response = await api.post('/api/auth/login', {
      username: config.username,
      password: config.password
    });
    
    authToken = response.data.token;
    console.log('✓ 登录成功');
    console.log(`  Token: ${authToken.substring(0, 20)}...`);
    console.log(`  用户信息：${JSON.stringify(response.data.userInfo)}`);
    
    return true;
  } catch (error) {
    console.log('✗ 登录失败:', error.response?.data?.msg || error.message);
    return false;
  }
}

async function testOrgTree() {
  console.log('\n======================================');
  console.log('2. 测试机构树接口');
  console.log('======================================');
  
  try {
    const response = await api.get('/api/sys/org/tree');
    console.log('✓ 获取机构树成功');
    console.log(`  机构数量：${response.data?.length || 0}`);
    if (response.data && response.data.length > 0) {
      console.log(`  第一个机构：${JSON.stringify(response.data[0])}`);
    }
    return true;
  } catch (error) {
    console.log('✗ 获取机构树失败:', error.response?.data?.msg || error.message);
    return false;
  }
}

async function testUserList() {
  console.log('\n======================================');
  console.log('3. 测试用户列表接口');
  console.log('======================================');
  
  try {
    const response = await api.get('/api/sys/user/list', {
      params: { pageNum: 1, pageSize: 10 }
    });
    console.log('✓ 获取用户列表成功');
    console.log(`  用户总数：${response.data?.total || 0}`);
    console.log(`  当前页数量：${response.data?.records?.length || 0}`);
    if (response.data?.records && response.data.records.length > 0) {
      console.log(`  第一个用户：${JSON.stringify(response.data.records[0])}`);
    }
    return true;
  } catch (error) {
    console.log('✗ 获取用户列表失败:', error.response?.data?.msg || error.message);
    return false;
  }
}

async function testMenuTree() {
  console.log('\n======================================');
  console.log('4. 测试菜单树接口');
  console.log('======================================');
  
  try {
    const response = await api.get('/api/sys/menu/tree');
    console.log('✓ 获取菜单树成功');
    console.log(`  根菜单数量：${response.data?.length || 0}`);
    if (response.data && response.data.length > 0) {
      console.log(`  第一个根菜单：${response.data[0].menuName} (${response.data[0].menuId})`);
    }
    return true;
  } catch (error) {
    console.log('✗ 获取菜单树失败:', error.response?.data?.msg || error.message);
    return false;
  }
}

async function testRoleList() {
  console.log('\n======================================');
  console.log('5. 测试角色列表接口');
  console.log('======================================');
  
  try {
    const response = await api.get('/api/sys/role/list', {
      params: { pageNum: 1, pageSize: 10 }
    });
    console.log('✓ 获取角色列表成功');
    console.log(`  角色总数：${response.data?.total || 0}`);
    console.log(`  当前页数量：${response.data?.records?.length || 0}`);
    if (response.data?.records && response.data.records.length > 0) {
      console.log(`  第一个角色：${JSON.stringify(response.data.records[0])}`);
    }
    return true;
  } catch (error) {
    console.log('✗ 获取角色列表失败:', error.response?.data?.msg || error.message);
    return false;
  }
}

async function testCreateOrg() {
  console.log('\n======================================');
  console.log('6. 测试新增机构接口');
  console.log('======================================');
  
  try {
    const testOrg = {
      orgCode: `test_org_${Date.now()}`,
      orgName: '测试机构',
      orgLevel: 2,
      parentOrgId: '1',
      sortOrder: 1,
      remark: '自动化测试创建的机构'
    };
    
    const response = await api.post('/api/sys/org', testOrg);
    console.log('✓ 新增机构成功');
    console.log(`  响应：${JSON.stringify(response)}`);
    return true;
  } catch (error) {
    console.log('✗ 新增机构失败:', error.response?.data?.msg || error.message);
    return false;
  }
}

async function testCreateUser() {
  console.log('\n======================================');
  console.log('7. 测试新增用户接口');
  console.log('======================================');
  
  try {
    const testUser = {
      userCode: `test_user_${Date.now()}`,
      userName: '测试用户',
      mobile: '13800138000',
      email: 'test@example.com',
      orgId: '1',
      roleId: '1',
      authLevel: 1,
      remark: '自动化测试创建的用户'
    };
    
    const response = await api.post('/api/sys/user', testUser);
    console.log('✓ 新增用户成功');
    console.log(`  响应：${JSON.stringify(response)}`);
    return true;
  } catch (error) {
    console.log('✗ 新增用户失败:', error.response?.data?.msg || error.message);
    return false;
  }
}

async function testCreateRole() {
  console.log('\n======================================');
  console.log('8. 测试新增角色接口');
  console.log('======================================');
  
  try {
    const testRole = {
      roleCode: `test_role_${Date.now()}`,
      roleName: '测试角色',
      roleDesc: '自动化测试创建的角色'
    };
    
    const response = await api.post('/api/sys/role', testRole);
    console.log('✓ 新增角色成功');
    console.log(`  响应：${JSON.stringify(response)}`);
    return true;
  } catch (error) {
    console.log('✗ 新增角色失败:', error.response?.data?.msg || error.message);
    return false;
  }
}

async function testCreateMenu() {
  console.log('\n======================================');
  console.log('9. 测试新增菜单接口');
  console.log('======================================');
  
  try {
    const testMenu = {
      menuName: '测试菜单',
      menuType: 2,
      parentMenuId: '1',
      menuUrl: '/test',
      menuIcon: 'test',
      perms: 'sys:test:view',
      sortOrder: 1,
      visible: 1
    };
    
    const response = await api.post('/api/sys/menu', testMenu);
    console.log('✓ 新增菜单成功');
    console.log(`  响应：${JSON.stringify(response)}`);
    return true;
  } catch (error) {
    console.log('✗ 新增菜单失败:', error.response?.data?.msg || error.message);
    return false;
  }
}

// 主测试流程
async function runTests() {
  console.log('\n╔══════════════════════════════════════════════════════════╗');
  console.log('║       第二阶段 API 接口自动化测试                         ║');
  console.log('╚══════════════════════════════════════════════════════════╝');
  console.log(`\n开始时间：${new Date().toLocaleString('zh-CN')}`);
  console.log(`服务地址：${BASE_URL}`);
  
  const results = {
    passed: 0,
    failed: 0,
    total: 0
  };

  const tests = [
    { name: '登录', fn: testLogin, required: true },
    { name: '机构树查询', fn: testOrgTree, required: true },
    { name: '用户列表查询', fn: testUserList, required: true },
    { name: '菜单树查询', fn: testMenuTree, required: true },
    { name: '角色列表查询', fn: testRoleList, required: true },
    { name: '新增机构', fn: testCreateOrg, required: false },
    { name: '新增用户', fn: testCreateUser, required: false },
    { name: '新增角色', fn: testCreateRole, required: false },
    { name: '新增菜单', fn: testCreateMenu, required: false }
  ];

  for (const test of tests) {
    results.total++;
    try {
      const success = await test.fn();
      if (success) {
        results.passed++;
      } else {
        results.failed++;
        if (test.required) {
          console.log(`\n⚠️  关键测试失败，后续测试跳过：${test.name}`);
          break;
        }
      }
    } catch (error) {
      results.failed++;
      console.log(`\n✗ 测试异常：${error.message}`);
      if (test.required) {
        console.log(`⚠️  关键测试异常，后续测试跳过`);
        break;
      }
    }
    
    // 添加延迟，避免请求过快
    await new Promise(resolve => setTimeout(resolve, 500));
  }

  // 输出测试报告
  console.log('\n\n╔══════════════════════════════════════════════════════════╗');
  console.log('║                    测试报告                                ║');
  console.log('╚══════════════════════════════════════════════════════════╝');
  console.log(`\n总测试数：${results.total}`);
  console.log(`✓ 通过：${results.passed}`);
  console.log(`✗ 失败：${results.failed}`);
  console.log(`\n结束时间：${new Date().toLocaleString('zh-CN')}`);
  
  if (results.failed === 0) {
    console.log('\n🎉 所有测试通过！');
  } else {
    console.log(`\n⚠️  有 ${results.failed} 个测试失败，请检查日志。`);
  }
  
  return results;
}

// 运行测试
runTests().catch(console.error);
