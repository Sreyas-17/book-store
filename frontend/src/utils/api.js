// src/utils/api.js - Fixed version that doesn't read response body twice
const API_BASE = 'http://localhost:8080/api';

export const apiCall = async (endpoint, options = {}, token = null) => {
  const url = `${API_BASE}${endpoint}`;
  
  // Build headers
  const headers = {
    'Content-Type': 'application/json',
  };
  
  // Add Authorization header if token is provided
  if (token) {
    headers.Authorization = `Bearer ${token}`;
  }
  
  const config = {
    headers,
    ...options,
  };

  // Debug logging
  console.log('🔄 API Call:', {
    url,
    method: config.method || 'GET',
    hasToken: !!token,
    tokenPreview: token ? token.substring(0, 20) + '...' : 'none'
  });

  try {
    const response = await fetch(url, config);
    
    // Log response details
    console.log('📥 API Response:', {
      url,
      status: response.status,
      statusText: response.statusText,
      ok: response.ok
    });
    
    // Get response text first, then try to parse as JSON
    const responseText = await response.text();
    console.log('📄 Raw response text:', responseText);
    
    // If empty response and status is OK, return success
    if (!responseText && response.ok) {
      return { success: true, message: 'Operation completed successfully' };
    }
    
    // Try to parse as JSON
    let data;
    try {
      data = JSON.parse(responseText);
      console.log('📊 Parsed JSON:', data);
    } catch (parseError) {
      console.error('❌ JSON Parse Error:', parseError);
      console.log('📄 Response was not valid JSON:', responseText);
      return { 
        success: false, 
        message: `Server returned non-JSON response: ${response.status} ${response.statusText}`,
        rawResponse: responseText
      };
    }
    
    // If response is not ok but we got JSON, return the JSON (might contain error info)
    if (!response.ok) {
      console.warn('⚠️ Response not OK but got JSON:', data);
      return data.success !== undefined ? data : { 
        success: false, 
        message: data.message || `HTTP ${response.status}: ${response.statusText}`,
        status: response.status
      };
    }
    
    // Return the parsed data
    return data;
    
  } catch (error) {
    console.error('❌ Network Error:', error);
    return { 
      success: false, 
      message: `Network error: ${error.message}` 
    };
  }
};