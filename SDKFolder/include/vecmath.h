/*==============================================================================
Copyright 2017 Maxst, Inc. All Rights Reserved.
==============================================================================*/


/*
* Copyright 2013 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*      http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

#ifndef VECMATH_H_
#define VECMATH_H_

#include <math.h>
#include <stdio.h>

#define PI 3.1415926535897932384626433832795f
#define TO_DEGREE 57.2958

namespace gl_helper
{
	/******************************************************************
	* Helper class for vector math operations
	* Currently all implementations are in pure C++.
	* Each class is an opaque class so caller does not have a direct access
	* to each element. This is for an ease of future optimization to use vector operations.
	*
	*/

	class Vec2;
	class Vec3;
	class Vec4;
	class Mat4;

	/******************************************************************
	* 2 elements vector class
	*
	*/
	class Vec2
	{
	public:
		float x_;
		float y_;

	public:
		friend class Vec3;
		friend class Vec4;
		friend class Mat4;
		friend class Quaternion;

		Vec2()
		{
			x_ = y_ = 0.f;
		}

		Vec2(const float fX, const float fY)
		{
			x_ = fX;
			y_ = fY;
		}

		Vec2(const Vec2& vec)
		{
			x_ = vec.x_;
			y_ = vec.y_;
		}

		Vec2(const float* pVec)
		{
			x_ = (*pVec++);
			y_ = (*pVec++);
		}

		//Operators
		Vec2 operator*(const Vec2& rhs) const
		{
			Vec2 ret;
			ret.x_ = x_ * rhs.x_;
			ret.y_ = y_ * rhs.y_;
			return ret;
		}

		Vec2 operator/(const Vec2& rhs) const
		{
			Vec2 ret;
			ret.x_ = x_ / rhs.x_;
			ret.y_ = y_ / rhs.y_;
			return ret;
		}

		Vec2 operator+(const Vec2& rhs) const
		{
			Vec2 ret;
			ret.x_ = x_ + rhs.x_;
			ret.y_ = y_ + rhs.y_;
			return ret;
		}

		Vec2 operator-(const Vec2& rhs) const
		{
			Vec2 ret;
			ret.x_ = x_ - rhs.x_;
			ret.y_ = y_ - rhs.y_;
			return ret;
		}

		Vec2& operator+=(const Vec2& rhs)
		{
			x_ += rhs.x_;
			y_ += rhs.y_;
			return *this;
		}

		Vec2& operator-=(const Vec2& rhs)
		{
			x_ -= rhs.x_;
			y_ -= rhs.y_;
			return *this;
		}

		Vec2& operator*=(const Vec2& rhs)
		{
			x_ *= rhs.x_;
			y_ *= rhs.y_;
			return *this;
		}

		Vec2& operator/=(const Vec2& rhs)
		{
			x_ /= rhs.x_;
			y_ /= rhs.y_;
			return *this;
		}

		//External operators
		friend Vec2 operator-(const Vec2& rhs)
		{
			return Vec2(rhs) *= -1;
		}

		friend Vec2 operator*(const float lhs, const Vec2& rhs)
		{
			Vec2 ret;
			ret.x_ = lhs * rhs.x_;
			ret.y_ = lhs * rhs.y_;
			return ret;
		}

		friend Vec2 operator/(const float lhs, const Vec2& rhs)
		{
			Vec2 ret;
			ret.x_ = lhs / rhs.x_;
			ret.y_ = lhs / rhs.y_;
			return ret;
		}

		// compare for order.     
		bool operator <(const Vec2& v) const
		{
			return (x_ < v.x_) || ((!(v.x_ < x_)) && (y_ < v.y_));
		}

		//Operators with float
		Vec2 operator*(const float& rhs) const
		{
			Vec2 ret;
			ret.x_ = x_ * rhs;
			ret.y_ = y_ * rhs;
			return ret;
		}

		Vec2& operator*=(const float& rhs)
		{
			x_ = x_ * rhs;
			y_ = y_ * rhs;
			return *this;
		}

		Vec2 operator/(const float& rhs) const
		{
			Vec2 ret;
			ret.x_ = x_ / rhs;
			ret.y_ = y_ / rhs;
			return ret;
		}

		Vec2& operator/=(const float& rhs)
		{
			x_ = x_ / rhs;
			y_ = y_ / rhs;
			return *this;
		}

		//Compare
		bool operator==(const Vec2& rhs) const
		{
			if (x_ != rhs.x_ || y_ != rhs.y_)
				return false;
			return true;
		}

		bool operator!=(const Vec2& rhs) const
		{
			if (x_ == rhs.x_)
				return false;

			return true;
		}

		float Length() const
		{
			return sqrtf(x_ * x_ + y_ * y_);
		}

		Vec2 Normalize()
		{
			float len = Length();
			x_ = x_ / len;
			y_ = y_ / len;
			return *this;
		}

		float Dot(const Vec2& rhs)
		{
			return x_ * rhs.x_ + y_ * rhs.y_;
		}

		float Distance(const Vec2& rhs)
		{
			return sqrtf(((x_ - rhs.x_) * (x_ - rhs.x_)) + ((y_ - rhs.y_) * (y_ - rhs.y_)));
		}

		bool Validate()
		{
			if (isnan(x_) || isnan(y_))
				return false;
			return true;
		}

		void Value(float& fX, float& fY)
		{
			fX = x_;
			fY = y_;
		}

		void Dump()
		{
			printf("Vec2 %f %f", x_, y_);
		}
	};

	/******************************************************************
	* 3 elements vector class
	*
	*/
	class Vec3
	{
	public:
		friend class Vec4;
		friend class Mat3;
		friend class Mat4;
		friend class Quaternion;

		float x_, y_, z_;

		Vec3()
		{
			x_ = y_ = z_ = 0.f;
		}

		Vec3(const float fX, const float fY, const float fZ)
		{
			x_ = fX;
			y_ = fY;
			z_ = fZ;
		}

		Vec3(const Vec3& vec)
		{
			x_ = vec.x_;
			y_ = vec.y_;
			z_ = vec.z_;
		}

		Vec3(const float* pVec)
		{
			x_ = (*pVec++);
			y_ = (*pVec++);
			z_ = *pVec;
		}

		Vec3(const Vec2& vec, float f)
		{
			x_ = vec.x_;
			y_ = vec.y_;
			z_ = f;
		}

		// compare for order.     
		bool operator <(const Vec3& v) const
		{
			return (x_ < v.x_) && (y_ < v.y_) && (z_ < v.z_);
		}

		//Operators
		Vec3 operator*(const Vec3& rhs) const
		{
			Vec3 ret;
			ret.x_ = x_ * rhs.x_;
			ret.y_ = y_ * rhs.y_;
			ret.z_ = z_ * rhs.z_;
			return ret;
		}

		Vec3 operator/(const Vec3& rhs) const
		{
			Vec3 ret;
			ret.x_ = x_ / rhs.x_;
			ret.y_ = y_ / rhs.y_;
			ret.z_ = z_ / rhs.z_;
			return ret;
		}

		Vec3 operator+(const Vec3& rhs) const
		{
			Vec3 ret;
			ret.x_ = x_ + rhs.x_;
			ret.y_ = y_ + rhs.y_;
			ret.z_ = z_ + rhs.z_;
			return ret;
		}

		Vec3 operator-(const Vec3& rhs) const
		{
			Vec3 ret;
			ret.x_ = x_ - rhs.x_;
			ret.y_ = y_ - rhs.y_;
			ret.z_ = z_ - rhs.z_;
			return ret;
		}

		Vec3& operator+=(const Vec3& rhs)
		{
			x_ += rhs.x_;
			y_ += rhs.y_;
			z_ += rhs.z_;
			return *this;
		}

		Vec3& operator-=(const Vec3& rhs)
		{
			x_ -= rhs.x_;
			y_ -= rhs.y_;
			z_ -= rhs.z_;
			return *this;
		}

		Vec3& operator*=(const Vec3& rhs)
		{
			x_ *= rhs.x_;
			y_ *= rhs.y_;
			z_ *= rhs.z_;
			return *this;
		}

		Vec3& operator/=(const Vec3& rhs)
		{
			x_ /= rhs.x_;
			y_ /= rhs.y_;
			z_ /= rhs.z_;
			return *this;
		}

		//External operators
		friend Vec3 operator-(const Vec3& rhs)
		{
			return Vec3(rhs) *= -1;
		}

		friend Vec3 operator*(const float lhs, const Vec3& rhs)
		{
			Vec3 ret;
			ret.x_ = lhs * rhs.x_;
			ret.y_ = lhs * rhs.y_;
			ret.z_ = lhs * rhs.z_;
			return ret;
		}

		friend Vec3 operator/(const float lhs, const Vec3& rhs)
		{
			Vec3 ret;
			ret.x_ = lhs / rhs.x_;
			ret.y_ = lhs / rhs.y_;
			ret.z_ = lhs / rhs.z_;
			return ret;
		}

		//Operators with float
		Vec3 operator*(const float& rhs) const
		{
			Vec3 ret;
			ret.x_ = x_ * rhs;
			ret.y_ = y_ * rhs;
			ret.z_ = z_ * rhs;
			return ret;
		}

		Vec3& operator*=(const float& rhs)
		{
			x_ = x_ * rhs;
			y_ = y_ * rhs;
			z_ = z_ * rhs;
			return *this;
		}

		Vec3 operator/(const float& rhs) const
		{
			Vec3 ret;
			ret.x_ = x_ / rhs;
			ret.y_ = y_ / rhs;
			ret.z_ = z_ / rhs;
			return ret;
		}

		Vec3& operator/=(const float& rhs)
		{
			x_ = x_ / rhs;
			y_ = y_ / rhs;
			z_ = z_ / rhs;
			return *this;
		}

		//Compare
		bool operator==(const Vec3& rhs) const
		{
			if (x_ != rhs.x_ || y_ != rhs.y_ || z_ != rhs.z_)
				return false;
			return true;
		}

		bool operator!=(const Vec3& rhs) const
		{
			if (x_ == rhs.x_)
				return false;

			return true;
		}

		float Length() const
		{
			return sqrtf(x_ * x_ + y_ * y_ + z_ * z_);
		}

		Vec3 Normalize()
		{
			float len = Length();
			x_ = x_ / len;
			y_ = y_ / len;
			z_ = z_ / len;
			return *this;
		}

		float Dot(const Vec3& rhs)
		{
			return x_ * rhs.x_ + y_ * rhs.y_ + z_ * rhs.z_;
		}

		float Distance(const Vec3& rhs)
		{
			return sqrtf(((x_ - rhs.x_) * (x_ - rhs.x_)) + ((y_ - rhs.y_) * (y_ - rhs.y_)) + ((z_ - rhs.z_) * (z_ - rhs.z_)));
		}

		Vec3 Cross(const Vec3& rhs)
		{
			Vec3 ret;
			ret.x_ = y_ * rhs.z_ - z_ * rhs.y_;
			ret.y_ = z_ * rhs.x_ - x_ * rhs.z_;
			ret.z_ = x_ * rhs.y_ - y_ * rhs.x_;
			return ret;
		}

		bool Validate()
		{
			if (isnan(x_) || isnan(y_) || isnan(z_))
				return false;
			return true;
		}

		void Value(float& fX, float& fY, float& fZ)
		{
			fX = x_;
			fY = y_;
			fZ = z_;
		}

		void Dump()
		{
			printf("Vec3 %f %f %f\n", x_, y_, z_);
		}
	};

	/******************************************************************
	* 4 elements vector class
	*
	*/
	class Vec4
	{
	public:
		float x_, y_, z_, w_;

	public:
		friend class Vec3;
		friend class Mat4;
		friend class Quaternion;

		Vec4()
		{
			x_ = y_ = z_ = w_ = 0.f;
		}

		Vec4(const float fX, const float fY, const float fZ, const float fW)
		{
			x_ = fX;
			y_ = fY;
			z_ = fZ;
			w_ = fW;
		}

		Vec4(const Vec4& vec)
		{
			x_ = vec.x_;
			y_ = vec.y_;
			z_ = vec.z_;
			w_ = vec.w_;
		}

		Vec4(const Vec3& vec, const float fW)
		{
			x_ = vec.x_;
			y_ = vec.y_;
			z_ = vec.z_;
			w_ = fW;
		}

		Vec4(const float* pVec)
		{
			x_ = (*pVec++);
			y_ = (*pVec++);
			z_ = *pVec;
			w_ = *pVec;
		}

		//Operators
		Vec4 operator*(const Vec4& rhs) const
		{
			Vec4 ret;
			ret.x_ = x_ * rhs.x_;
			ret.y_ = y_ * rhs.y_;
			ret.z_ = z_ * rhs.z_;
			ret.w_ = z_ * rhs.w_;
			return ret;
		}

		Vec4 operator/(const Vec4& rhs) const
		{
			Vec4 ret;
			ret.x_ = x_ / rhs.x_;
			ret.y_ = y_ / rhs.y_;
			ret.z_ = z_ / rhs.z_;
			ret.w_ = z_ / rhs.w_;
			return ret;
		}

		Vec4 operator+(const Vec4& rhs) const
		{
			Vec4 ret;
			ret.x_ = x_ + rhs.x_;
			ret.y_ = y_ + rhs.y_;
			ret.z_ = z_ + rhs.z_;
			ret.w_ = z_ + rhs.w_;
			return ret;
		}

		Vec4 operator-(const Vec4& rhs) const
		{
			Vec4 ret;
			ret.x_ = x_ - rhs.x_;
			ret.y_ = y_ - rhs.y_;
			ret.z_ = z_ - rhs.z_;
			ret.w_ = z_ - rhs.w_;
			return ret;
		}

		Vec4& operator+=(const Vec4& rhs)
		{
			x_ += rhs.x_;
			y_ += rhs.y_;
			z_ += rhs.z_;
			w_ += rhs.w_;
			return *this;
		}

		Vec4& operator-=(const Vec4& rhs)
		{
			x_ -= rhs.x_;
			y_ -= rhs.y_;
			z_ -= rhs.z_;
			w_ -= rhs.w_;
			return *this;
		}

		Vec4& operator*=(const Vec4& rhs)
		{
			x_ *= rhs.x_;
			y_ *= rhs.y_;
			z_ *= rhs.z_;
			w_ *= rhs.w_;
			return *this;
		}

		Vec4& operator/=(const Vec4& rhs)
		{
			x_ /= rhs.x_;
			y_ /= rhs.y_;
			z_ /= rhs.z_;
			w_ /= rhs.w_;
			return *this;
		}

		//External operators
		friend Vec4 operator-(const Vec4& rhs)
		{
			return Vec4(rhs) *= -1;
		}

		friend Vec4 operator*(const float lhs, const Vec4& rhs)
		{
			Vec4 ret;
			ret.x_ = lhs * rhs.x_;
			ret.y_ = lhs * rhs.y_;
			ret.z_ = lhs * rhs.z_;
			ret.w_ = lhs * rhs.w_;
			return ret;
		}

		friend Vec4 operator/(const float lhs, const Vec4& rhs)
		{
			Vec4 ret;
			ret.x_ = lhs / rhs.x_;
			ret.y_ = lhs / rhs.y_;
			ret.z_ = lhs / rhs.z_;
			ret.w_ = lhs / rhs.w_;
			return ret;
		}

		//Operators with float
		Vec4 operator*(const float& rhs) const
		{
			Vec4 ret;
			ret.x_ = x_ * rhs;
			ret.y_ = y_ * rhs;
			ret.z_ = z_ * rhs;
			ret.w_ = w_ * rhs;
			return ret;
		}

		Vec4& operator*=(const float& rhs)
		{
			x_ = x_ * rhs;
			y_ = y_ * rhs;
			z_ = z_ * rhs;
			w_ = w_ * rhs;
			return *this;
		}

		Vec4 operator/(const float& rhs) const
		{
			Vec4 ret;
			ret.x_ = x_ / rhs;
			ret.y_ = y_ / rhs;
			ret.z_ = z_ / rhs;
			ret.w_ = w_ / rhs;
			return ret;
		}

		Vec4& operator/=(const float& rhs)
		{
			x_ = x_ / rhs;
			y_ = y_ / rhs;
			z_ = z_ / rhs;
			w_ = w_ / rhs;
			return *this;
		}

		//Compare
		bool operator==(const Vec4& rhs) const
		{
			if (x_ != rhs.x_ || y_ != rhs.y_ || z_ != rhs.z_ || w_ != rhs.w_)
				return false;
			return true;
		}

		bool operator!=(const Vec4& rhs) const
		{
			if (x_ == rhs.x_)
				return false;

			return true;
		}

		float Length() const
		{
			return sqrtf(x_ * x_ + y_ * y_ + z_ * z_ + w_ * w_);
		}

		Vec4 Normalize()
		{
			float len = Length();
			x_ = x_ / len;
			y_ = y_ / len;
			z_ = z_ / len;
			w_ = w_ / len;
			return *this;
		}

		float Dot(const Vec3& rhs)
		{
			return x_ * rhs.x_ + y_ * rhs.y_ + z_ * rhs.z_;
		}

		Vec3 Cross(const Vec3& rhs)
		{
			Vec3 ret;
			ret.x_ = y_ * rhs.z_ - z_ * rhs.y_;
			ret.y_ = z_ * rhs.x_ - x_ * rhs.z_;
			ret.z_ = x_ * rhs.y_ - y_ * rhs.x_;
			return ret;
		}

		bool Validate()
		{
			if (isnan(x_) || isnan(y_) || isnan(z_) || isnan(w_))
				return false;
			return true;
		}

		void Value(float& fX, float& fY, float& fZ, float& fW)
		{
			fX = x_;
			fY = y_;
			fZ = z_;
			fW = w_;
		}
	};

	/******************************************************************
	* 3x3 column major Matrix
	*
	*/
	class Mat3
	{
	private:
		float f_[9];

	public:
		friend class Vec3;

		Mat3() {
			for (int i = 0; i < 9; i++) {
				f_[i] = 0.f;
			}
		}

		Mat3(const float* mIn) {
			for (int i = 0; i < 9; i++) {
				f_[i] = mIn[i];
			}
		}

		Mat3 operator*(const Mat3& rhs) const {
			Mat3 ret;

			ret.f_[0] = f_[0] * rhs.f_[0] + f_[3] * rhs.f_[1] + f_[6] * rhs.f_[2];
			ret.f_[1] = f_[1] * rhs.f_[0] + f_[4] * rhs.f_[1] + f_[7] * rhs.f_[2];
			ret.f_[2] = f_[2] * rhs.f_[0] + f_[5] * rhs.f_[1] + f_[8] * rhs.f_[2];

			ret.f_[3] = f_[0] * rhs.f_[3] + f_[3] * rhs.f_[4] + f_[6] * rhs.f_[5];
			ret.f_[4] = f_[1] * rhs.f_[3] + f_[4] * rhs.f_[4] + f_[7] * rhs.f_[5];
			ret.f_[5] = f_[2] * rhs.f_[3] + f_[5] * rhs.f_[4] + f_[8] * rhs.f_[5];

			ret.f_[6] = f_[0] * rhs.f_[6] + f_[3] * rhs.f_[7] + f_[6] * rhs.f_[8];
			ret.f_[7] = f_[1] * rhs.f_[6] + f_[4] * rhs.f_[7] + f_[7] * rhs.f_[8];
			ret.f_[8] = f_[2] * rhs.f_[6] + f_[5] * rhs.f_[7] + f_[8] * rhs.f_[8];

			return ret;
		}

		Vec3 operator*(const Vec3& rhs) const {
			Vec3 ret;
			ret.x_ = rhs.x_ * f_[0] + rhs.y_ * f_[3] + rhs.z_ * f_[6];
			ret.y_ = rhs.x_ * f_[1] + rhs.y_ * f_[4] + rhs.z_ * f_[7];
			ret.z_ = rhs.x_ * f_[2] + rhs.y_ * f_[5] + rhs.z_ * f_[8];
			return ret;
		}

		Mat3 operator+(const Mat3& rhs) const {
			Mat3 ret;
			for (int i = 0; i < 9; ++i) {
				ret.f_[i] = f_[i] + rhs.f_[i];
			}
			return ret;
		}

		Mat3 operator-(const Mat3& rhs) const {
			Mat3 ret;
			for (int i = 0; i < 9; ++i) {
				ret.f_[i] = f_[i] - rhs.f_[i];
			}
			return ret;
		}

		Mat3& operator+=(const Mat3& rhs) {
			for (int i = 0; i < 9; ++i) {
				f_[i] += rhs.f_[i];
			}
			return *this;
		}

		Mat3& operator-=(const Mat3& rhs) {
			for (int i = 0; i < 9; ++i) {
				f_[i] -= rhs.f_[i];
			}
			return *this;
		}

		Mat3& operator*=(const Mat3& rhs) {
			Mat3 ret;

			ret.f_[0] = f_[0] * rhs.f_[0] + f_[3] * rhs.f_[1] + f_[6] * rhs.f_[2];
			ret.f_[1] = f_[1] * rhs.f_[0] + f_[4] * rhs.f_[1] + f_[7] * rhs.f_[2];
			ret.f_[2] = f_[2] * rhs.f_[0] + f_[5] * rhs.f_[1] + f_[8] * rhs.f_[2];

			ret.f_[3] = f_[0] * rhs.f_[3] + f_[3] * rhs.f_[4] + f_[6] * rhs.f_[5];
			ret.f_[4] = f_[1] * rhs.f_[3] + f_[4] * rhs.f_[4] + f_[7] * rhs.f_[5];
			ret.f_[5] = f_[2] * rhs.f_[3] + f_[5] * rhs.f_[4] + f_[8] * rhs.f_[5];

			ret.f_[6] = f_[0] * rhs.f_[6] + f_[3] * rhs.f_[7] + f_[6] * rhs.f_[8];
			ret.f_[7] = f_[1] * rhs.f_[6] + f_[4] * rhs.f_[7] + f_[7] * rhs.f_[8];
			ret.f_[8] = f_[2] * rhs.f_[6] + f_[5] * rhs.f_[7] + f_[8] * rhs.f_[8];

			*this = ret;
			return *this;
		}

		Mat3 operator*(const float rhs) {
			Mat3 ret;
			for (int i = 0; i < 9; ++i) {
				ret.f_[i] = f_[i] * rhs;
			}
			return ret;
		}

		Mat3& operator*=(const float rhs) {
			for (int i = 0; i < 9; ++i) {
				f_[i] *= rhs;
			}
			return *this;
		}

		Mat3& operator=(const Mat3& rhs) {
			for (int i = 0; i < 9; ++i) {
				f_[i] = rhs.f_[i];
			}
			return *this;
		}

		Mat3 Inverse() {
			Mat3 ret;

			float det =
				f_[0] * f_[4] * f_[8] +
				f_[1] * f_[5] * f_[6] +
				f_[2] * f_[3] * f_[7] -
				f_[0] * f_[5] * f_[7] -
				f_[2] * f_[4] * f_[6] -
				f_[1] * f_[3] * f_[8];

			if (det == 0) {
				//Error
			}
			else {
				ret.f_[0] = (f_[4] * f_[8] - f_[7] * f_[5]) / det;
				ret.f_[1] = (f_[7] * f_[2] - f_[1] * f_[8]) / det;
				ret.f_[2] = (f_[1] * f_[5] - f_[4] * f_[2]) / det;

				ret.f_[3] = (f_[6] * f_[5] - f_[3] * f_[8]) / det;
				ret.f_[4] = (f_[0] * f_[8] - f_[6] * f_[2]) / det;
				ret.f_[5] = (f_[3] * f_[2] - f_[0] * f_[5]) / det;

				ret.f_[6] = (f_[3] * f_[7] - f_[6] * f_[4]) / det;
				ret.f_[7] = (f_[6] * f_[1] - f_[0] * f_[7]) / det;
				ret.f_[8] = (f_[0] * f_[4] - f_[3] * f_[1]) / det;
			}

			*this = ret;
			return *this;
		}

		Mat3 Transpose() {
			Mat3 ret;
			ret.f_[0] = f_[0];
			ret.f_[1] = f_[3];
			ret.f_[2] = f_[6];

			ret.f_[3] = f_[1];
			ret.f_[4] = f_[4];
			ret.f_[5] = f_[7];

			ret.f_[6] = f_[2];
			ret.f_[7] = f_[5];
			ret.f_[8] = f_[8];

			*this = ret;
			return *this;
		}

		float* Ptr() {
			return f_;
		}


		static Mat3 Identity() {
			Mat3 ret;

			ret.f_[0] = 1.f;
			ret.f_[1] = 0.f;
			ret.f_[2] = 0.f;
			ret.f_[3] = 0.f;
			ret.f_[4] = 1.f;
			ret.f_[5] = 0.f;
			ret.f_[6] = 0.f;
			ret.f_[7] = 0.f;
			ret.f_[8] = 1.f;

			return ret;
		}

		static Mat3 Zero() {
			Mat3 ret;

			ret.f_[0] = 0.0f;
			ret.f_[1] = 0.0f;
			ret.f_[2] = 0.0f;
			ret.f_[3] = 0.0f;
			ret.f_[4] = 0.0f;
			ret.f_[5] = 0.0f;
			ret.f_[6] = 0.0f;
			ret.f_[7] = 0.0f;
			ret.f_[8] = 0.0f;

			return ret;
		}
	};

	/******************************************************************
	* 4x4 matrix
	*
	*/
	class Mat4
	{
	private:
		float f_[16];

	public:
		friend class Vec3;
		friend class Vec4;
		friend class Quaternion;

		Mat4()
		{
			for (int i = 0; i < 16; ++i)
				f_[i] = 0.f;
		}

		Mat4(const float* mIn)
		{
			for (int i = 0; i < 16; ++i)
				f_[i] = mIn[i];
		}

		Mat4 operator*(const Mat4& rhs) const
		{
			Mat4 ret;
			ret.f_[0] = f_[0] * rhs.f_[0] + f_[4] * rhs.f_[1] + f_[8] * rhs.f_[2]
				+ f_[12] * rhs.f_[3];
			ret.f_[1] = f_[1] * rhs.f_[0] + f_[5] * rhs.f_[1] + f_[9] * rhs.f_[2]
				+ f_[13] * rhs.f_[3];
			ret.f_[2] = f_[2] * rhs.f_[0] + f_[6] * rhs.f_[1] + f_[10] * rhs.f_[2]
				+ f_[14] * rhs.f_[3];
			ret.f_[3] = f_[3] * rhs.f_[0] + f_[7] * rhs.f_[1] + f_[11] * rhs.f_[2]
				+ f_[15] * rhs.f_[3];

			ret.f_[4] = f_[0] * rhs.f_[4] + f_[4] * rhs.f_[5] + f_[8] * rhs.f_[6]
				+ f_[12] * rhs.f_[7];
			ret.f_[5] = f_[1] * rhs.f_[4] + f_[5] * rhs.f_[5] + f_[9] * rhs.f_[6]
				+ f_[13] * rhs.f_[7];
			ret.f_[6] = f_[2] * rhs.f_[4] + f_[6] * rhs.f_[5] + f_[10] * rhs.f_[6]
				+ f_[14] * rhs.f_[7];
			ret.f_[7] = f_[3] * rhs.f_[4] + f_[7] * rhs.f_[5] + f_[11] * rhs.f_[6]
				+ f_[15] * rhs.f_[7];

			ret.f_[8] = f_[0] * rhs.f_[8] + f_[4] * rhs.f_[9] + f_[8] * rhs.f_[10]
				+ f_[12] * rhs.f_[11];
			ret.f_[9] = f_[1] * rhs.f_[8] + f_[5] * rhs.f_[9] + f_[9] * rhs.f_[10]
				+ f_[13] * rhs.f_[11];
			ret.f_[10] = f_[2] * rhs.f_[8] + f_[6] * rhs.f_[9] + f_[10] * rhs.f_[10]
				+ f_[14] * rhs.f_[11];
			ret.f_[11] = f_[3] * rhs.f_[8] + f_[7] * rhs.f_[9] + f_[11] * rhs.f_[10]
				+ f_[15] * rhs.f_[11];

			ret.f_[12] = f_[0] * rhs.f_[12] + f_[4] * rhs.f_[13] + f_[8] * rhs.f_[14]
				+ f_[12] * rhs.f_[15];
			ret.f_[13] = f_[1] * rhs.f_[12] + f_[5] * rhs.f_[13] + f_[9] * rhs.f_[14]
				+ f_[13] * rhs.f_[15];
			ret.f_[14] = f_[2] * rhs.f_[12] + f_[6] * rhs.f_[13] + f_[10] * rhs.f_[14]
				+ f_[14] * rhs.f_[15];
			ret.f_[15] = f_[3] * rhs.f_[12] + f_[7] * rhs.f_[13] + f_[11] * rhs.f_[14]
				+ f_[15] * rhs.f_[15];

			return ret;
		}

		Vec4 operator*(const Vec4& rhs) const
		{
			Vec4 ret;
			ret.x_ = rhs.x_ * f_[0] + rhs.y_ * f_[4] + rhs.z_ * f_[8] + rhs.w_ * f_[12];
			ret.y_ = rhs.x_ * f_[1] + rhs.y_ * f_[5] + rhs.z_ * f_[9] + rhs.w_ * f_[13];
			ret.z_ = rhs.x_ * f_[2] + rhs.y_ * f_[6] + rhs.z_ * f_[10] + rhs.w_ * f_[14];
			ret.w_ = rhs.x_ * f_[3] + rhs.y_ * f_[7] + rhs.z_ * f_[11] + rhs.w_ * f_[15];
			return ret;
		}

		Mat4 operator+(const Mat4& rhs) const
		{
			Mat4 ret;
			for (int i = 0; i < 16; ++i)
			{
				ret.f_[i] = f_[i] + rhs.f_[i];
			}
			return ret;
		}

		Mat4 operator-(const Mat4& rhs) const
		{
			Mat4 ret;
			for (int i = 0; i < 16; ++i)
			{
				ret.f_[i] = f_[i] - rhs.f_[i];
			}
			return ret;
		}

		Mat4& operator+=(const Mat4& rhs)
		{
			for (int i = 0; i < 16; ++i)
			{
				f_[i] += rhs.f_[i];
			}
			return *this;
		}

		Mat4& operator-=(const Mat4& rhs)
		{
			for (int i = 0; i < 16; ++i)
			{
				f_[i] -= rhs.f_[i];
			}
			return *this;
		}

		Mat4& operator*=(const Mat4& rhs)
		{
			Mat4 ret;
			ret.f_[0] = f_[0] * rhs.f_[0] + f_[4] * rhs.f_[1] + f_[8] * rhs.f_[2]
				+ f_[12] * rhs.f_[3];
			ret.f_[1] = f_[1] * rhs.f_[0] + f_[5] * rhs.f_[1] + f_[9] * rhs.f_[2]
				+ f_[13] * rhs.f_[3];
			ret.f_[2] = f_[2] * rhs.f_[0] + f_[6] * rhs.f_[1] + f_[10] * rhs.f_[2]
				+ f_[14] * rhs.f_[3];
			ret.f_[3] = f_[3] * rhs.f_[0] + f_[7] * rhs.f_[1] + f_[11] * rhs.f_[2]
				+ f_[15] * rhs.f_[3];

			ret.f_[4] = f_[0] * rhs.f_[4] + f_[4] * rhs.f_[5] + f_[8] * rhs.f_[6]
				+ f_[12] * rhs.f_[7];
			ret.f_[5] = f_[1] * rhs.f_[4] + f_[5] * rhs.f_[5] + f_[9] * rhs.f_[6]
				+ f_[13] * rhs.f_[7];
			ret.f_[6] = f_[2] * rhs.f_[4] + f_[6] * rhs.f_[5] + f_[10] * rhs.f_[6]
				+ f_[14] * rhs.f_[7];
			ret.f_[7] = f_[3] * rhs.f_[4] + f_[7] * rhs.f_[5] + f_[11] * rhs.f_[6]
				+ f_[15] * rhs.f_[7];

			ret.f_[8] = f_[0] * rhs.f_[8] + f_[4] * rhs.f_[9] + f_[8] * rhs.f_[10]
				+ f_[12] * rhs.f_[11];
			ret.f_[9] = f_[1] * rhs.f_[8] + f_[5] * rhs.f_[9] + f_[9] * rhs.f_[10]
				+ f_[13] * rhs.f_[11];
			ret.f_[10] = f_[2] * rhs.f_[8] + f_[6] * rhs.f_[9] + f_[10] * rhs.f_[10]
				+ f_[14] * rhs.f_[11];
			ret.f_[11] = f_[3] * rhs.f_[8] + f_[7] * rhs.f_[9] + f_[11] * rhs.f_[10]
				+ f_[15] * rhs.f_[11];

			ret.f_[12] = f_[0] * rhs.f_[12] + f_[4] * rhs.f_[13] + f_[8] * rhs.f_[14]
				+ f_[12] * rhs.f_[15];
			ret.f_[13] = f_[1] * rhs.f_[12] + f_[5] * rhs.f_[13] + f_[9] * rhs.f_[14]
				+ f_[13] * rhs.f_[15];
			ret.f_[14] = f_[2] * rhs.f_[12] + f_[6] * rhs.f_[13] + f_[10] * rhs.f_[14]
				+ f_[14] * rhs.f_[15];
			ret.f_[15] = f_[3] * rhs.f_[12] + f_[7] * rhs.f_[13] + f_[11] * rhs.f_[14]
				+ f_[15] * rhs.f_[15];

			*this = ret;
			return *this;
		}

		Mat4 operator*(const float rhs)
		{
			Mat4 ret;
			for (int i = 0; i < 16; ++i)
			{
				ret.f_[i] = f_[i] * rhs;
			}
			return ret;
		}

		Mat4& operator*=(const float rhs)
		{
			for (int i = 0; i < 16; ++i)
			{
				f_[i] *= rhs;
			}
			return *this;
		}

		Mat4& operator=(const Mat4& rhs)
		{
			for (int i = 0; i < 16; ++i)
			{
				f_[i] = rhs.f_[i];
			}
			return *this;
		}

		Mat4 Inverse()
		{
			Mat4 ret;
			float det_1;
			float pos = 0;
			float neg = 0;
			float temp;

			temp = f_[0] * f_[5] * f_[10];
			if (temp >= 0)
				pos += temp;
			else
				neg += temp;
			temp = f_[4] * f_[9] * f_[2];
			if (temp >= 0)
				pos += temp;
			else
				neg += temp;
			temp = f_[8] * f_[1] * f_[6];
			if (temp >= 0)
				pos += temp;
			else
				neg += temp;
			temp = -f_[8] * f_[5] * f_[2];
			if (temp >= 0)
				pos += temp;
			else
				neg += temp;
			temp = -f_[4] * f_[1] * f_[10];
			if (temp >= 0)
				pos += temp;
			else
				neg += temp;
			temp = -f_[0] * f_[9] * f_[6];
			if (temp >= 0)
				pos += temp;
			else
				neg += temp;
			det_1 = pos + neg;

			if (det_1 == 0.0)
			{
				//Error
			}
			else
			{
				det_1 = 1.0f / det_1;
				ret.f_[0] = (f_[5] * f_[10] - f_[9] * f_[6]) * det_1;
				ret.f_[1] = -(f_[1] * f_[10] - f_[9] * f_[2]) * det_1;
				ret.f_[2] = (f_[1] * f_[6] - f_[5] * f_[2]) * det_1;
				ret.f_[4] = -(f_[4] * f_[10] - f_[8] * f_[6]) * det_1;
				ret.f_[5] = (f_[0] * f_[10] - f_[8] * f_[2]) * det_1;
				ret.f_[6] = -(f_[0] * f_[6] - f_[4] * f_[2]) * det_1;
				ret.f_[8] = (f_[4] * f_[9] - f_[8] * f_[5]) * det_1;
				ret.f_[9] = -(f_[0] * f_[9] - f_[8] * f_[1]) * det_1;
				ret.f_[10] = (f_[0] * f_[5] - f_[4] * f_[1]) * det_1;

				/* Calculate -C * inverse(A) */
				ret.f_[12] = -(f_[12] * ret.f_[0] + f_[13] * ret.f_[4] + f_[14] * ret.f_[8]);
				ret.f_[13] = -(f_[12] * ret.f_[1] + f_[13] * ret.f_[5] + f_[14] * ret.f_[9]);
				ret.f_[14] = -(f_[12] * ret.f_[2] + f_[13] * ret.f_[6] + f_[14] * ret.f_[10]);

				ret.f_[3] = 0.0f;
				ret.f_[7] = 0.0f;
				ret.f_[11] = 0.0f;
				ret.f_[15] = 1.0f;
			}

			*this = ret;
			return *this;
		}

		Mat4 Transpose()
		{
			Mat4 ret;
			ret.f_[0] = f_[0];
			ret.f_[1] = f_[4];
			ret.f_[2] = f_[8];
			ret.f_[3] = f_[12];
			ret.f_[4] = f_[1];
			ret.f_[5] = f_[5];
			ret.f_[6] = f_[9];
			ret.f_[7] = f_[13];
			ret.f_[8] = f_[2];
			ret.f_[9] = f_[6];
			ret.f_[10] = f_[10];
			ret.f_[11] = f_[14];
			ret.f_[12] = f_[3];
			ret.f_[13] = f_[7];
			ret.f_[14] = f_[11];
			ret.f_[15] = f_[15];
			*this = ret;
			return *this;
		}

		Mat4& PostTranslate(float tx, float ty, float tz)
		{
			f_[12] += (tx * f_[0]) + (ty * f_[4]) + (tz * f_[8]);
			f_[13] += (tx * f_[1]) + (ty * f_[5]) + (tz * f_[9]);
			f_[14] += (tx * f_[2]) + (ty * f_[6]) + (tz * f_[10]);
			f_[15] += (tx * f_[3]) + (ty * f_[7]) + (tz * f_[11]);
			return *this;
		}

		Mat4& PostRotate(float angle, float rx, float ry, float rz)
		{
			float sinAngle, cosAngle;
			float mag = (float)sqrtf(rx * rx + ry * ry + rz * rz);

			sinAngle = sinf(angle * PI / 180.0f);
			cosAngle = cosf(angle * PI / 180.0f);

			if (mag > 0.0f) {
				float xx, yy, zz, xy, yz, zx, xs, ys, zs;
				float oneMinusCos;
				Mat4 rotMat;

				rx /= mag;
				ry /= mag;
				rz /= mag;

				xx = rx * rx;
				yy = ry * ry;
				zz = rz * rz;
				xy = rx * ry;
				yz = ry * rz;
				zx = rz * rx;
				xs = rx * sinAngle;
				ys = ry * sinAngle;
				zs = rz * sinAngle;

				oneMinusCos = 1.0f - cosAngle;
				rotMat.Ptr()[0] = (oneMinusCos * xx) + cosAngle;
				rotMat.Ptr()[1] = (oneMinusCos * xy) - zs;
				rotMat.Ptr()[2] = (oneMinusCos * zx) + ys;
				rotMat.Ptr()[3] = 0.0F;
				rotMat.Ptr()[4] = (oneMinusCos * xy) + zs;
				rotMat.Ptr()[5] = (oneMinusCos * yy) + cosAngle;
				rotMat.Ptr()[6] = (oneMinusCos * yz) - xs;
				rotMat.Ptr()[7] = 0.0F;
				rotMat.Ptr()[8] = (oneMinusCos * zx) - ys;
				rotMat.Ptr()[9] = (oneMinusCos * yz) + xs;
				rotMat.Ptr()[10] = (oneMinusCos * zz) + cosAngle;
				rotMat.Ptr()[11] = 0.0F;
				rotMat.Ptr()[12] = 0.0F;
				rotMat.Ptr()[13] = 0.0F;
				rotMat.Ptr()[14] = 0.0F;
				rotMat.Ptr()[15] = 1.0F;

				*this *= rotMat;
			}
			return *this;
		}

		Mat4& PostScale(float sx, float sy, float sz)
		{
			f_[0] *= sx;
			f_[1] *= sx;
			f_[2] *= sx;
			f_[3] *= sx;
			f_[4] *= sy;
			f_[5] *= sy;
			f_[6] *= sy;
			f_[7] *= sy;
			f_[8] *= sz;
			f_[9] *= sz;
			f_[10] *= sz;
			f_[11] *= sz;
			return *this;
		}

		float* Ptr()
		{
			return f_;
		}

		//--------------------------------------------------------------------------------
		// Misc
		//--------------------------------------------------------------------------------
		static Mat4 Perspective(float width, float height, float nearPlane, float farPlane)
		{
			Mat4 result;
#if 1
			float x = 1.0f;
			float y = width / height;

			result.Ptr()[0] = 2.0f * x;
			result.Ptr()[1] = 0.0f;
			result.Ptr()[2] = 0.0f;
			result.Ptr()[3] = 0.0f;
            
			result.Ptr()[4] = 0.0f;
			result.Ptr()[5] = 2.0f * -y;
			result.Ptr()[6] = 0.0f;
			result.Ptr()[7] = 0.0f;
            
			result.Ptr()[8] = 0.0f;
			result.Ptr()[9] = 0.0f;
			result.Ptr()[10] = (farPlane + nearPlane) / (farPlane - nearPlane);
			result.Ptr()[11] = 1.0f;
            
			result.Ptr()[12] = 0.0f;
			result.Ptr()[13] = 0.0f;
			result.Ptr()[14] = -(2.0f * farPlane * nearPlane) / (farPlane - nearPlane);
			result.Ptr()[15] = 0.0f;
#else
			float n2 = 2.0f * nearPlane;
			float rcpnmf = 1.f / (nearPlane - farPlane);

			Mat4 result;
			result.f_[0] = n2 / width;
			result.f_[4] = 0;
			result.f_[8] = 0;
			result.f_[12] = 0;
			result.f_[1] = 0;
			result.f_[5] = n2 / height;
			result.f_[9] = 0;
			result.f_[13] = 0;
			result.f_[2] = 0;
			result.f_[6] = 0;
			result.f_[10] = (farPlane + nearPlane) * rcpnmf;
			result.f_[14] = farPlane * rcpnmf * n2;
			result.f_[3] = 0;
			result.f_[7] = 0;
			result.f_[11] = -1.0;
			result.f_[15] = 0;
#endif
			return result;
		}

		static Mat4 Perspective(float fovy, int width, int height, float zNear, float zFar)
		{
			Mat4 result;

			float aspect = (float)width / (float)height;
			float f = 1.0f / (float)tan(fovy * (PI / 360.0f));
			float rangeReciprocal = 1.0f / (zNear - zFar);

			result.f_[0] = f / aspect;
			result.f_[1] = 0.0f;
			result.f_[2] = 0.0f;
			result.f_[3] = 0.0f;

			result.f_[4] = 0.0f;
			result.f_[5] = -f;
			result.f_[6] = 0.0f;
			result.f_[7] = 0.0f;

			result.f_[8] = 0.0f;
			result.f_[9] = 0.0f;
			result.f_[10] = -(zFar + zNear) * rangeReciprocal;
			result.f_[11] = 1.0f;

			result.f_[12] = 0.0f;
			result.f_[13] = 0.0f;
			result.f_[14] = 2.0f * zFar * zNear * rangeReciprocal;
			result.f_[15] = 0.0f;

			return result;
		}

		static Mat4 Perspective(float fx, float fy, float cx, float cy, int w, int h, float n, float f)
		{
			Mat4 result;

			result.f_[0] = 2.0f * fx / w;
			result.f_[1] = 0.0f;
			result.f_[2] = 0.0f;
			result.f_[3] = 0.0f;

			result.f_[4] = 0.0f;
			result.f_[5] = -2.0f * fy / h;
			result.f_[6] = 0.0f;
			result.f_[7] = 0.0f;

			result.f_[8] = 1.0f - 2.0f * cx / w;
			result.f_[9] = 2.0f * cy / h - 1.0f;
			result.f_[10] = -(f + n) / (n - f);
			result.f_[11] = 1.0f;

			result.f_[12] = 0.0f;
			result.f_[13] = 0.0f;
			result.f_[14] = 2.0f * f * n / (n - f);
			result.f_[15] = 0.0f;

#if 0
			m[0][0] = 2.0 * fx / width;
			m[0][1] = 0.0;
			m[0][2] = 0.0;
			m[0][3] = 0.0;

			m[1][0] = 0.0;
			m[1][1] = -2.0 * fy / height;
			m[1][2] = 0.0;
			m[1][3] = 0.0;

			m[2][0] = 1.0 - 2.0 * cx / width;
			m[2][1] = 2.0 * cy / height - 1.0;
			m[2][2] = (zfar + znear) / (znear - zfar);
			m[2][3] = -1.0;

			m[3][0] = 0.0;
			m[3][1] = 0.0;
			m[3][2] = 2.0 * zfar * znear / (znear - zfar);
			m[3][3] = 0.0;
#endif

			return result;
		}

		static Mat4 Ortho(float left, float right, float bottom, float top, float nearPlane, float farPlane)
		{
			float r_l = right - left;
			float t_b = top - bottom;
			float f_n = farPlane - nearPlane;

			float tx = -(right + left) / (right - left);
			float ty = -(top + bottom) / (top - bottom);
			float tz = -(farPlane + nearPlane) / (farPlane - nearPlane);

			Mat4 result;
			result.f_[0] = 2.0f / r_l;
			result.f_[1] = 0.0f;
			result.f_[2] = 0.0f;
			result.f_[3] = tx;

			result.f_[4] = 0.0f;
			result.f_[5] = 2.0f / t_b;
			result.f_[6] = 0.0f;
			result.f_[7] = ty;

			result.f_[8] = 0.0f;
			result.f_[9] = 0.0f;
			result.f_[10] = 2.0f / f_n;
			result.f_[11] = tz;

			result.f_[12] = 0.0f;
			result.f_[13] = 0.0f;
			result.f_[14] = 0.0f;
			result.f_[15] = 1.0f;

			return result;
		}

		static Mat4 LookAt(const Vec3& vec_eye, const Vec3& vec_at, const Vec3& vec_up)
		{
			Vec3 vec_forward, vec_up_norm, vec_side;
			Mat4 result;

			vec_forward.x_ = vec_eye.x_ - vec_at.x_;
			vec_forward.y_ = vec_eye.y_ - vec_at.y_;
			vec_forward.z_ = vec_eye.z_ - vec_at.z_;

			vec_forward.Normalize();
			vec_up_norm = vec_up;
			vec_up_norm.Normalize();
			vec_side = vec_up_norm.Cross(vec_forward);
			vec_up_norm = vec_forward.Cross(vec_side);

			result.f_[0] = vec_side.x_;
			result.f_[4] = vec_side.y_;
			result.f_[8] = vec_side.z_;
			result.f_[12] = 0;
			result.f_[1] = vec_up_norm.x_;
			result.f_[5] = vec_up_norm.y_;
			result.f_[9] = vec_up_norm.z_;
			result.f_[13] = 0;
			result.f_[2] = vec_forward.x_;
			result.f_[6] = vec_forward.y_;
			result.f_[10] = vec_forward.z_;
			result.f_[14] = 0;
			result.f_[3] = 0;
			result.f_[7] = 0;
			result.f_[11] = 0;
			result.f_[15] = 1.0;

			result.PostTranslate(-vec_eye.x_, -vec_eye.y_, -vec_eye.z_);
			return result;
		}

		static Mat4 Translation(const float fX, const float fY, const float fZ)
		{
			Mat4 ret;
			ret.f_[0] = 1.0f;
			ret.f_[4] = 0.0f;
			ret.f_[8] = 0.0f;
			ret.f_[12] = fX;
			ret.f_[1] = 0.0f;
			ret.f_[5] = 1.0f;
			ret.f_[9] = 0.0f;
			ret.f_[13] = fY;
			ret.f_[2] = 0.0f;
			ret.f_[6] = 0.0f;
			ret.f_[10] = 1.0f;
			ret.f_[14] = fZ;
			ret.f_[3] = 0.0f;
			ret.f_[7] = 0.0f;
			ret.f_[11] = 0.0f;
			ret.f_[15] = 1.0f;
			return ret;
		}

		static Mat4 Translation(const Vec3 vec)
		{
			Mat4 ret;
			ret.f_[0] = 1.0f;
			ret.f_[4] = 0.0f;
			ret.f_[8] = 0.0f;
			ret.f_[12] = vec.x_;
			ret.f_[1] = 0.0f;
			ret.f_[5] = 1.0f;
			ret.f_[9] = 0.0f;
			ret.f_[13] = vec.y_;
			ret.f_[2] = 0.0f;
			ret.f_[6] = 0.0f;
			ret.f_[10] = 1.0f;
			ret.f_[14] = vec.z_;
			ret.f_[3] = 0.0f;
			ret.f_[7] = 0.0f;
			ret.f_[11] = 0.0f;
			ret.f_[15] = 1.0f;
			return ret;
		}

		static Mat4 RotationX(const float fAngle)
		{
			Mat4 ret;
			float fCosine, fSine;

			fCosine = cosf(fAngle);
			fSine = sinf(fAngle);

			ret.f_[0] = 1.0f;
			ret.f_[4] = 0.0f;
			ret.f_[8] = 0.0f;
			ret.f_[12] = 0.0f;
			ret.f_[1] = 0.0f;
			ret.f_[5] = fCosine;
			ret.f_[9] = fSine;
			ret.f_[13] = 0.0f;
			ret.f_[2] = 0.0f;
			ret.f_[6] = -fSine;
			ret.f_[10] = fCosine;
			ret.f_[14] = 0.0f;
			ret.f_[3] = 0.0f;
			ret.f_[7] = 0.0f;
			ret.f_[11] = 0.0f;
			ret.f_[15] = 1.0f;
			return ret;
		}

		static Mat4 RotationY(const float fAngle)
		{
			Mat4 ret;
			float fCosine, fSine;

			fCosine = cosf(fAngle);
			fSine = sinf(fAngle);

			ret.f_[0] = fCosine;
			ret.f_[4] = 0.0f;
			ret.f_[8] = -fSine;
			ret.f_[12] = 0.0f;
			ret.f_[1] = 0.0f;
			ret.f_[5] = 1.0f;
			ret.f_[9] = 0.0f;
			ret.f_[13] = 0.0f;
			ret.f_[2] = fSine;
			ret.f_[6] = 0.0f;
			ret.f_[10] = fCosine;
			ret.f_[14] = 0.0f;
			ret.f_[3] = 0.0f;
			ret.f_[7] = 0.0f;
			ret.f_[11] = 0.0f;
			ret.f_[15] = 1.0f;
			return ret;
		}

		static Mat4 RotationZ(const float fAngle)
		{
			Mat4 ret;
			float fCosine, fSine;

			fCosine = cosf(fAngle);
			fSine = sinf(fAngle);

			ret.f_[0] = fCosine;
			ret.f_[4] = fSine;
			ret.f_[8] = 0.0f;
			ret.f_[12] = 0.0f;
			ret.f_[1] = -fSine;
			ret.f_[5] = fCosine;
			ret.f_[9] = 0.0f;
			ret.f_[13] = 0.0f;
			ret.f_[2] = 0.0f;
			ret.f_[6] = 0.0f;
			ret.f_[10] = 1.0f;
			ret.f_[14] = 0.0f;
			ret.f_[3] = 0.0f;
			ret.f_[7] = 0.0f;
			ret.f_[11] = 0.0f;
			ret.f_[15] = 1.0f;
			return ret;
		}

		static Mat4 Scale(const float fX, const float fY, const float fZ)
		{
			Mat4 ret;
			ret.f_[0] = fX;
			ret.f_[1] = 0;
			ret.f_[2] = 0;
			ret.f_[3] = 0;
			ret.f_[4] = 0;
			ret.f_[5] = fY;
			ret.f_[6] = 0;
			ret.f_[7] = 0;
			ret.f_[8] = 0;
			ret.f_[9] = 0;
			ret.f_[10] = fZ;
			ret.f_[11] = 0;
			ret.f_[12] = 0;
			ret.f_[13] = 0;
			ret.f_[14] = 0;
			ret.f_[15] = 1.f;
			return ret;
		}

		static Mat4 Identity()
		{
			Mat4 ret;
			ret.f_[0] = 1.f;
			ret.f_[1] = 0;
			ret.f_[2] = 0;
			ret.f_[3] = 0;
			ret.f_[4] = 0;
			ret.f_[5] = 1.f;
			ret.f_[6] = 0;
			ret.f_[7] = 0;
			ret.f_[8] = 0;
			ret.f_[9] = 0;
			ret.f_[10] = 1.f;
			ret.f_[11] = 0;
			ret.f_[12] = 0;
			ret.f_[13] = 0;
			ret.f_[14] = 0;
			ret.f_[15] = 1.f;
			return ret;
		}

		static Mat4 Zero()
		{
			Mat4 ret;

			ret.f_[0] = 0.0f;
			ret.f_[1] = 0.0f;
			ret.f_[2] = 0.0f;
			ret.f_[3] = 0.0f;
			ret.f_[4] = 0.0f;
			ret.f_[5] = 0.0f;
			ret.f_[6] = 0.0f;
			ret.f_[7] = 0.0f;
			ret.f_[8] = 0.0f;
			ret.f_[9] = 0.0f;
			ret.f_[10] = 0.0f;
			ret.f_[11] = 0.0f;
			ret.f_[12] = 0.0f;
			ret.f_[13] = 0.0f;
			ret.f_[14] = 0.0f;
			ret.f_[15] = 0.0f;
			return ret;
		}

		void Dump()
		{
			printf("%f %f %f %f\n", f_[0], f_[1], f_[2], f_[3]);
			printf("%f %f %f %f\n", f_[4], f_[5], f_[6], f_[7]);
			printf("%f %f %f %f\n", f_[8], f_[9], f_[10], f_[11]);
			printf("%f %f %f %f\n", f_[12], f_[13], f_[14], f_[15]);
		}
	};

	/******************************************************************
	* Quaternion class
	*
	*/
	class Quaternion
	{
	private:
		float x_, y_, z_, w_;

	public:
		friend class Vec3;
		friend class Vec4;
		friend class Mat4;

		Quaternion()
		{
			x_ = 0.f;
			y_ = 0.f;
			z_ = 0.f;
			w_ = 1.f;
		}

		Quaternion(const float fX, const float fY, const float fZ, const float fW)
		{
			x_ = fX;
			y_ = fY;
			z_ = fZ;
			w_ = fW;
		}

		Quaternion(const Vec3 vec, const float fW)
		{
			x_ = vec.x_;
			y_ = vec.y_;
			z_ = vec.z_;
			w_ = fW;
		}

		Quaternion(const float* p)
		{
			x_ = *p++;
			y_ = *p++;
			z_ = *p++;
			w_ = *p++;
		}

		Quaternion operator*(const Quaternion rhs)
		{
			Quaternion ret;
			ret.x_ = x_ * rhs.w_ + y_ * rhs.z_ - z_ * rhs.y_ + w_ * rhs.x_;
			ret.y_ = -x_ * rhs.z_ + y_ * rhs.w_ + z_ * rhs.x_ + w_ * rhs.y_;
			ret.z_ = x_ * rhs.y_ - y_ * rhs.x_ + z_ * rhs.w_ + w_ * rhs.z_;
			ret.w_ = -x_ * rhs.x_ - y_ * rhs.y_ - z_ * rhs.z_ + w_ * rhs.w_;
			return ret;
		}

		Quaternion& operator*=(const Quaternion rhs)
		{
			Quaternion ret;
			ret.x_ = x_ * rhs.w_ + y_ * rhs.z_ - z_ * rhs.y_ + w_ * rhs.x_;
			ret.y_ = -x_ * rhs.z_ + y_ * rhs.w_ + z_ * rhs.x_ + w_ * rhs.y_;
			ret.z_ = x_ * rhs.y_ - y_ * rhs.x_ + z_ * rhs.w_ + w_ * rhs.z_;
			ret.w_ = -x_ * rhs.x_ - y_ * rhs.y_ - z_ * rhs.z_ + w_ * rhs.w_;
			*this = ret;
			return *this;
		}

		Quaternion Conjugate()
		{
			x_ = -x_;
			y_ = -y_;
			z_ = -z_;
			return *this;
		}

		//Non destuctive version
		Quaternion Conjugated()
		{
			Quaternion ret;
			ret.x_ = -x_;
			ret.y_ = -y_;
			ret.z_ = -z_;
			ret.w_ = w_;
			return ret;
		}

		void ToMatrix(Mat4& mat)
		{
			float x2 = x_ * x_ * 2.0f;
			float y2 = y_ * y_ * 2.0f;
			float z2 = z_ * z_ * 2.0f;
			float xy = x_ * y_ * 2.0f;
			float yz = y_ * z_ * 2.0f;
			float zx = z_ * x_ * 2.0f;
			float xw = x_ * w_ * 2.0f;
			float yw = y_ * w_ * 2.0f;
			float zw = z_ * w_ * 2.0f;

			mat.f_[0] = 1.0f - y2 - z2;
			mat.f_[1] = xy + zw;
			mat.f_[2] = zx - yw;
			mat.f_[4] = xy - zw;
			mat.f_[5] = 1.0f - z2 - x2;
			mat.f_[6] = yz + xw;
			mat.f_[8] = zx + yw;
			mat.f_[9] = yz - xw;
			mat.f_[10] = 1.0f - x2 - y2;

			mat.f_[3] = mat.f_[7] = mat.f_[11] = mat.f_[12] = mat.f_[13] = mat.f_[14] = 0.0f;
			mat.f_[15] = 1.0f;
		}

		void ToMatrixPreserveTranslate(Mat4& mat)
		{
			float x2 = x_ * x_ * 2.0f;
			float y2 = y_ * y_ * 2.0f;
			float z2 = z_ * z_ * 2.0f;
			float xy = x_ * y_ * 2.0f;
			float yz = y_ * z_ * 2.0f;
			float zx = z_ * x_ * 2.0f;
			float xw = x_ * w_ * 2.0f;
			float yw = y_ * w_ * 2.0f;
			float zw = z_ * w_ * 2.0f;

			mat.f_[0] = 1.0f - y2 - z2;
			mat.f_[1] = xy + zw;
			mat.f_[2] = zx - yw;
			mat.f_[4] = xy - zw;
			mat.f_[5] = 1.0f - z2 - x2;
			mat.f_[6] = yz + xw;
			mat.f_[8] = zx + yw;
			mat.f_[9] = yz - xw;
			mat.f_[10] = 1.0f - x2 - y2;

			mat.f_[3] = mat.f_[7] = mat.f_[11] = 0.0f;
			mat.f_[15] = 1.0f;
		}

		static Quaternion RotationAxis(const Vec3 axis, const float angle)
		{
			Quaternion ret;
			float s = sinf(angle / 2);
			ret.x_ = s * axis.x_;
			ret.y_ = s * axis.y_;
			ret.z_ = s * axis.z_;
			ret.w_ = cosf(angle / 2);
			return ret;
		}

		void Value(float& fX, float& fY, float& fZ, float& fW)
		{
			fX = x_;
			fY = y_;
			fZ = z_;
			fW = w_;
		}
	};

} //namespace ndk_helper
#endif /* VECMATH_H_ */
