import axios from './axios'; // 确保使用配置了baseURL的axios实例

export const getUserInfo = async () => {
    try {
        const response = await axios.get('/user-info', {
            headers: {
                'Authorization': `Bearer ${localStorage.getItem('token')}`
            }
        });
        return response.data;
    } catch (error) {
        console.error('Failed to fetch user info:', error);
        throw error;
    }
};