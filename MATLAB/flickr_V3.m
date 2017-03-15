set(gcf,'color',[1, 1, 1]);
set(gcf,'unit','centimeters','position',[10 5 9 7]);

x=[1, 2, 3, 4, 5];
y1 = [968.4931305700001	923.12142042	915.66037454	915.2668816400001	1011.63289702];
y2 = [4303.23745919	3626.7391646300002	3621.3010882500002	3618.97603816	4524.01978966];
y3 = [22.38667889	21.367256360000003	20.85101066	22.08802087 26.90619781 ];

p1= semilogy(x, y1, '-k^');
hold on;
p2 = semilogy(x, y2, '-ko');
hold on;
p3 = semilogy(x, y3, '-kd');

xlabel('the length of query set in Q');
ylabel('time (ms)');

axis([0.5 5.5 10.0 10000]);

set(gca, 'xtick', 1:5, 'XTickLabel', {'2','3','4','5','6'});
leg=legend('basic-g-v2','basic-w-v2','MDec', 2);
set(leg,'edgecolor','white');

set(gca, 'FontSize', 12);
set(get(gca, 'XLabel'), 'FontSize',12);
set(get(gca, 'YLabel'), 'FontSize',12);
set(findall(gcf,'type','line'),'linewidth',1.5);
set(gca, 'LineWidth', 1.5);