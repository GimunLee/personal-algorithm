package boj.mst;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.StringTokenizer;

/**
 * Prim : 340ms
 * kruscal : 540ms (height 추가)
 */
public class Main_1922_네트워크연결_kruscal {
	private static int N, M;
	private static int[][] map;

	public static void main(String[] args) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		N = Integer.parseInt(br.readLine().trim()); // 컴퓨터의 수
		M = Integer.parseInt(br.readLine().trim()); // 연결할 수 있는 선의 수

		map = new int[N + 1][N + 1];
		Edge[] weight = new Edge[M];

		for (int i = 0; i < M; i++) {
			StringTokenizer st = new StringTokenizer(br.readLine(), " ");

			int s = Integer.parseInt(st.nextToken());
			int e = Integer.parseInt(st.nextToken()); // 연결 컴퓨터 (s <-> e)
			int c = Integer.parseInt(st.nextToken()); // 비용

			map[s][e] = c;
			map[e][s] = c;
			weight[i] = new Edge(s, e, c);
		}

		Arrays.sort(weight);

		p = new int[N + 1];
		height = new int[N + 1];

		for (int i = 1; i <= N; i++) {
			p[i] = i; // 최초엔 자기 자신이 부모
		}

		int ANSER = kruscal(weight);
		System.out.println(ANSER);

	} // end of main

	private static int kruscal(Edge[] weight) {
		int ANSER = 0;
		int cnt = N - 1;
		for (int i = 0; i < weight.length; i++) {
			Edge edge = weight[i];
			int node1 = edge.s;
			int node2 = edge.e;

			int p1 = find(node1);
			int p2 = find(node2);
			if (p1 != p2) {
				union(p1, p2);
				ANSER += edge.c;
				if (--cnt == 0) {
					return ANSER;
				}
			}
		}
		return ANSER;
	}

	private static int[] p;

	/** 속한 그룹 찾기 */
	private static int find(int num) {
		if (p[num] == num) {
			return p[num];
		} else {
			return p[num] = find(p[num]);
		}
	}

	private static int[] height;

	private static void union(int num1, int num2) {

		int p1 = find(num1);
		int p2 = find(num2);

		if (p1 == p2) {
			return;
		}

		if (height[p1] < height[p2]) {
			p[num2] = p[num1];
		} else {
			p[num1] = p[num2];
			height[p1] += 1;
		}
		return;
	}

	private static class Edge implements Comparable<Edge> {
		int s, e, c;

		public Edge(int s, int e, int c) {
			this.s = s;
			this.e = e;
			this.c = c;
		}

		@Override
		public int compareTo(Edge o) {
			return this.c - o.c;
		}
	}

} // end of Class
